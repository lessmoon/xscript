package parser;

import lexer.*;
import symbols.*;
import inter.util.*;
import inter.expr.*;
import inter.stmt.*;
import runtime.LoadFunc;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class Parser{
    private Lexer lex;
    private Token look;
    Env top = new Env();
    HashMap<Token,Type> typetable = new HashMap<Token,Type>();
    FuncTable table = new FuncTable();
    Type returnType = Type.Int;
    boolean hasDecl = true;
    boolean enableWarning = false;
    /*integer numbers standing for the stack level*/

    public int lastBreakFatherLevel = -1;
    public int lastIterationLevel = -1;
    /* The variable lastFunctionLevel is for the  
     * local function definition.
     * For now,it is just 0(which means global level).
     */
    public int lastFunctionLevel = 0;
    public int nowLevel = 0;
    public HashSet<FunctionBasic> f_used = new HashSet<FunctionBasic>();
    
    public final boolean ENABLE_EXPR_OPT ;
    public final boolean ENABLE_STMT_OPT ;
    public boolean PRINT_FUNC_TRANSLATE = false;

    public  Parser(Lexer l) throws IOException{
        this(l,false,false);
    }

    public  Parser(Lexer l,boolean expr_opt,boolean stmt_opt) throws IOException{
        lex = l;
        move();
        top.put(Word.args,new Array(Type.Str,0));
        ENABLE_EXPR_OPT = expr_opt;
        ENABLE_STMT_OPT = stmt_opt;
    }

    public void enablePrintFuncTranslate(){
        PRINT_FUNC_TRANSLATE = true;
    }

    public void disablePrintFuncTranslate(){
        PRINT_FUNC_TRANSLATE = false;
    }

    public void checkNamespace(Token name,String info){
        if( top.get(name) != null ){
            error(info + " `" + name + "' has a same name with a variable");
        }

        if( getType(name) != null ){
            error(info + " `" + name + "' has a same name with a struct");
        }
    }

    public void move() throws IOException {
        look = lex.scan();
    }

    public Token copymove() throws IOException {
        Token tmp = look;
        move();
        return tmp;
    }

    public void error(String s){
        error(s,lex.line,lex.filename);
    }
    
    public void error(String s,int l,String f){
        throw new RuntimeException("line " + l + " in file `" +  f + "':\n\t" + s);
    }
    
    public void warning(String s){
        warning(s,lex.line,lex.filename);
    }

    public void warning(String s,int l,String f){
        if(enableWarning)
            System.err.println("line " + l + " in file `" +  f + "':\n\t" + s);
    }

    public void match(char t) throws IOException{
        if(look.tag == t)
            move();
        else {
            if(look.tag == -1){
                error("unexpected end of file");
            }
            error("syntax error:expect `" + t +"',but found `" + look + "'");
        }
    }
        
    public void match(int t) throws IOException{
        if(look.tag == t)
            move();
        else {
            if(look.tag == -1){
                error("unexpected end of file");
            }
            String expect ;
            switch(t){
            case Tag.ID:
                expect = "identifier";
                break;
            case Tag.BASIC:
                expect = "basic type(includes (big)int,(big)real,char,bool and string)";
                break;
            case Tag.STR:
                 expect = "string constant";
                 break;
            default:
                expect = "";
            }
            error("syntax error: expect " + expect + ",but found `" + look + "'");
        }
    }

    public boolean check(int t) throws IOException{
        if(look.tag == t){
            move();
            return true;
        } else {
            return false;
        }
    }

    public Stmt program() throws IOException {
        Stmt s = Stmt.Null;
        while(look.tag != -1){
            switch(look.tag){
            case Tag.DEF:
                defFunction();
                break;
            case Tag.STRUCT:
                defStruct();
                break;
            case Tag.LDFUNC:
                loadFunction();
                break;
            case Tag.IMPORT:
                importLib();
                break;
            default:
                s = new Seq(s,stmt());
                break;
            }
        }

        checkFunctionCompletion();
        return ENABLE_STMT_OPT?s.optimize():s;
    }

    private void checkFunctionCompletion(){
        /*check if some used functions has not been completed*/
        for(FunctionBasic b : table.getAllFunctions()){
            if(b.used()&&!b.isCompleted()){
                error("function `" + b +"' used but not completed " ,b.lexline,b.filename);
            }
        }
        
        for(FunctionBasic b : f_used){
            if(b.used()&&!b.isCompleted()){
                error("function `" + b +"' used but not completed " ,b.lexline,b.filename);
            }
        }
        
        /*
         * check if there is a struct is pure virtual but declared
         */
        Iterator<Entry<Token,Type>> iter = typetable.entrySet().iterator();
        while(iter.hasNext()){
            Entry<Token,Type> info = iter.next();
            Struct st = (Struct)(info.getValue());
            if(st.used() && !st.isCompleted()){
                error("struct `" + info.getValue() +"' used but not completed " ,st.getFirstUsedLine(),st.getFirstUsedFile());
            }
        }
    }

    public void importLib() throws IOException {
        match(Tag.IMPORT);
        Token l = look;
        match(Tag.STR);
        if(look.tag != ';'){
            error("syntax error: expect `;',but found `" + look + "'");
        }
        lex.open(((Str)l).value);
        /*
         * ignore the `;'
         */
        move();
    }

    public void loadFunction() throws IOException {
        ArrayList<Para> pl  = null;
        match(Tag.LDFUNC);
        StringBuffer sb = new StringBuffer();
        match('<');
        Token pkg = look;
        match(Tag.ID);
        sb.append(pkg.toString());
        while(check('.')){
            sb.append(".");
            pkg = look;
            match(Tag.ID);
            sb.append(pkg.toString());
        }
        match('>');
        match('{');
        while(!check('}')){
            pl = new ArrayList<Para>();
            Type t = type();
            Token name = look;
            match(Tag.ID);
            match('(');
            if(!check(')')){
                do{
                    Type vt = type();
                    Token n = look;
                    match(Tag.ID);
                    pl.add(new Para(vt,n));
                }while(check(','));
                match(')');
            }
            
            FunctionBasic f = null;
            try{
                f = LoadFunc.loadFunc(t,sb.toString(),name,pl);
            } catch (Exception e){
                error("failed to load extension function `" + sb.toString()  + "." + name + "'");
            }
            if(!table.addFunc(name,f)){
                error("function name has conflict:" + name);
            }

            match(';');
        }
    }

    public Type defType(Token tok,Type t) {
        return typetable.put(tok,t);
    }
    
    public Type getType(Token tok){
        return typetable.get(tok);
    }
    
    public void defStruct() throws IOException {
        match(Tag.STRUCT);

        /*
         * Struct definition:
         *      struct name {
         *          type name;[...]
         *      }
         */
        
        Token name = look;
        match(Tag.ID);
        Struct s;
        if(check(':')){
            Token base = look;
            match(Tag.ID);
            Type b = typetable.get(base);
            if(b == null){
                error("base struct `" + base + "' not found");
            }
            s = new Struct(name,(Struct)b);
        } else {
            s = new Struct(name);
        }
        
        checkNamespace(name,"struct");
        defType(name,s);
        match('{');
        while(!check('}')){
            Token op = null;
            if(check('@')){
                Token tmp = look;
                if( look.tag == Tag.ID ){
                    op = getType(look);
                    if(op == null){
                        error("type `" + look + "' not found" );
                    }
                }  else {
                    op = copymove();
                }

                if(op.tag == Tag.BASIC && look.tag == '['){
                    Type t = arrtype((Type)op);
                    error("can't overloading array type `" + t + "' castint operand");
                }
            }
            /*Function definition*/
            if(check(Tag.DEF)){
                defInnerStructFunction(s,op);
            } else {
                if(op != null){
                    error("overloading for `" + op + "' found but no function definition found");
                }
                Type t = type();
                Token m = look;
                match(Tag.ID);
                if(s.addMemberVariable(m,t) != null){
                    error("member `" + m.toString() + "' defined previously ");
                }
                match(';');
            }
        }
    }
    
    /*
     * def a function in struct
     * s is the struct,op is the operand to overload(null for non-overloading)
     */
    public void defInnerStructFunction(Struct s,Token op) throws IOException{
        int flag = 0;
        
        if(check(Tag.VIRTUAL)){
            flag = Tag.VIRTUAL;
        } else if(check(Tag.OVERRIDE)){
            flag = Tag.OVERRIDE;
        } 

        Env savedEnv = top;
        top = new Env(top);
        boolean savedHasDecl = hasDecl;
        hasDecl = true;
        Type savedType = returnType;
        returnType = type();
        Token fname = look;
        match(Tag.ID);

        /*pass `this' reference as the first argument*/
        top.put(Word.This,s);
        ArrayList<Para> l = arguments();
        l.add(0,new Para(s,Word.This));
        Function f = new MemberFunction(fname,returnType,l,s);
        if(flag == Tag.VIRTUAL ){
            s.defineVirtualFunction(fname,f);
        } else if(flag == Tag.OVERRIDE){
            s.overrideVirtualFunction(fname,f);
        } else {
            s.addNormalFunction(fname,f);
        }
        if(op != null){
            if(!s.addOverloading(op,f)){
                error("operand `" + op + "' overloading is redefined");
            }
        }

        if(!check(';')){
            match('{');
            Stmt stmt = stmts();
            match('}');
            f.init(fname,returnType,stmt,l);
        }

        top = savedEnv;
        returnType = savedType;
        hasDecl = savedHasDecl;
    }

    public void defFunction() throws IOException {
        match(Tag.DEF);
        Env savedEnv = top;
        top = new Env(top);
        boolean savedHasDecl = hasDecl;
        hasDecl = true;
        Type savedType = returnType;
        returnType = type();
        Token name = look;
        match(Tag.ID);

        if(check('.')){
            Type t = getType(name);
            if(t == null){
                error("struct `" + name + "' not found");
            }

            if(!(t instanceof Struct)){
                error("struct type needed here,but `" + t + "' found");
            }
            name = look;
            match(Tag.ID);
            defOutterStructFunction((Struct) t,name,returnType);
            top = savedEnv;
            returnType = savedType;
            hasDecl = savedHasDecl;
            return;
        }

        ArrayList<Para> l = arguments();
        Function f = null;
        if(check(';')){
            f = new Function(name,returnType,l);
            /*check if its name has been used*/
            if(!table.addFunc(name,f)){
                error("function name has conflict:" + f );
            }
        } else {
            FunctionBasic fb = table.getFuncType(name);
            if(fb != null){
                if(fb.isCompleted()){
                    error("function redefined:" + fb );
                }
                f = (Function)fb;
                if(!f.type.equals(returnType)){
                    error("function return type doesn't match with its former declaration:" + f + "\nnote: " + returnType);
                }
                if(f.paralist.size() != l.size()){
                    error("function parameters number doesn't match with its former declaration :" + new Function(name,returnType,l).toString() + " has "  + l.size() +  ",but " + f + " has " + f.paralist.size());
                }
                int i = 0;
                for(Para t : f.paralist){
                    if(!t.type.equals(l.get(i).type)){
                        error("function " + (new Function(name,returnType,l)).toString() + " has different arguments["+ i +"] types with its former declaration " + f);
                    }
                    i++;
                }
            } else {
                f = new Function(name,returnType,l);
                /*check if its name has been used*/
                if(!table.addFunc(name,f)){
                    error("Function name has conflict:" + f );
                }
            }
            match('{');
            Stmt s = stmts();
            if(ENABLE_STMT_OPT)
                s = s.optimize();
            match('}');
            f.init(name,returnType,s,l);
            if(PRINT_FUNC_TRANSLATE){
                System.out.println(f.toString() +"{");
                System.out.print(s.toString());
                System.out.println("}" );
            }
        }

        top = savedEnv;
        returnType = savedType;
        hasDecl = savedHasDecl;
        return;
    }

    public void defOutterStructFunction(Struct t,Token name,Type returnType) throws IOException{
        //TODO:check if it is normal or virtual function
        
        Function f = (Function)t.getDeclaredFunction(name);
        if(f == null){
            error("member function declaration `" +t.lexeme + "." + name + "' not found ");
        }
        /*
         * NOTE:type.equals doesn't conclude 
         * that father struct and child struct is the same
         */
        if(!f.type.equals(returnType)){
            error("member function `" + t.lexeme + "." + name + "' return type doesn't match with the former declaration");
        }
        if(f.isCompleted()){
            error("member function `" + t.lexeme + "." + name  + "' redefined");
        }

        top.put(Word.This,t);
        ArrayList<Para> l = arguments();
        l.add(0,new Para(t,Word.This));
        if(l.size() != f.paralist.size()){
            error("parameters number of function `" + t.lexeme + "." + name  + "' doesn't match with its former declaration");
        }
        for(int i = 1;i < l.size();i++){
            if(!l.get(i).type.equals(f.paralist.get(i).type)){
                error("function `" + t.lexeme + "." + name  + "' has different arguments["+ (i-1) +"] type `" + l.get(i).type + "' with its former declaration `" + f.paralist.get(i).type + "'");
            }
        }
        match('{');
        Stmt stmt = stmts();
        match('}');
        f.init(name,returnType,stmt,l);
    }

    public ArrayList<Para> arguments() throws IOException {
        ArrayList<Para> pl = new ArrayList<Para>();
        match('(');
        if(!check(')')){
            do{
                Type t = type();
                Token name = look;
                match(Tag.ID);
                if( getType(name) != null ){
                    error("argument `" + name + "' shadows a struct type");
                }
                pl.add(new Para(t , name));
                if( top.put( name , t ) != null ){
                    error("function parameters names have conflict:`"+ name.toString() + "'" );
                }
            }while(check(','));
            match(')');
        }
        return pl;
    }

    public Stmt block() throws IOException {
        match('{');
        Env savedEnv = top;
        boolean savedHasDecl = hasDecl;
        hasDecl = false;
        //top = new Env(top);/*should not*/
        
        Stmt s = stmts();
        match('}');
        if(hasDecl){
            s = new Seq(s,Stmt.RecoverStack);
            nowLevel--;
            top = savedEnv;
        }
        hasDecl = savedHasDecl;
        return s;
    }

    public Stmt stmts() throws IOException {
        if(look.tag == '}' ) {
            return Stmt.Null;
        } else {
            return new Seq(stmt(),stmts());
        }
    }
    
    public Stmt stmt() throws IOException {
        Expr x;
        Stmt s,s1,s2;
        Stmt savedStmt = Stmt.Enclosing,savedBreak = Stmt.BreakEnclosing;
        int savedLastIterationLevel = lastIterationLevel,
            savedlastBreakFatherLevel = lastBreakFatherLevel;
        switch(look.tag){
        case ';':
            move();
            s = Stmt.Null;
            break;
        case Tag.IF:
            match(Tag.IF);
            match('(');
            x = expr();
            match(')');
            s1 = closure();
            if(look.tag == Tag.ELSE){
                match(Tag.ELSE);
                s2 = stmt();
                s = new Else(x,s1,s2);
            } else {
                s = new If(x,s1);
            }
            break;
        case Tag.WHILE:
            lastIterationLevel = nowLevel;
            lastBreakFatherLevel = nowLevel;
            While whilenode = new While();
            Stmt.Enclosing = whilenode;
            Stmt.BreakEnclosing = whilenode;
            match(Tag.WHILE);
            match('(');
            x = expr();
            match(')');
            s1 = closure();
            whilenode.init(x,s1);
            s =  whilenode;
            break;
        case Tag.DO:
            lastIterationLevel = nowLevel;
            lastBreakFatherLevel = nowLevel;
            Do donode = new Do();
            Stmt.Enclosing = donode;
            Stmt.BreakEnclosing = donode;
            match(Tag.DO);
            s1 = closure();
            match(Tag.WHILE);
            match('(');
            x = expr();
            match(')');
            match(';');
            donode.init(s1,x);
            s =  donode;
            break;
        case Tag.FOR:
            s = forloop();
            break;
        case Tag.BREAK:
            match(Tag.BREAK);
            match(';');
            s = new Break(nowLevel - lastBreakFatherLevel);
            break;
        case Tag.CONTINUE:
            match(Tag.CONTINUE);
            match(';');
            s = new Continue(nowLevel - lastIterationLevel);
            break;
        case Tag.RETURN:
            match(Tag.RETURN);
            Expr e = Expr.VoidExpr;
            if(returnType != Type.Void)
                e = expr();
            match(';');
            s = new Return(e,returnType,nowLevel - lastFunctionLevel);
            break;
        case '{':
            s = block();
            break;
        case Tag.ID:
            Type t = getType(look);
            /*check if it is just a variable*/
            if(t == null){
                s = new ExprStmt(expr());
                match(';');
                break;
            }
            /*It should be variable definition*/
            look = t;
        case Tag.BASIC:
            if(hasDecl){
                s = decls();
            } else {
                top = new Env(top);
                nowLevel ++;
                hasDecl = true;
                s = new Seq(Stmt.PushStack,decls());
            }
            break;
        case Tag.SWITCH:
            s = switchstmt();
            break;
        case Tag.DEFAULT:
        case Tag.CASE:/*CASE*/
             error("`" + look + "' should be used in switch statement");
            s = Stmt.Null;
            break;
        default:
            /*single expression statement*/
            s = new ExprStmt(expr());
            match(';');
        }
        Stmt.BreakEnclosing = savedBreak;
        Stmt.Enclosing = savedStmt;
        lastIterationLevel = savedLastIterationLevel;
        lastBreakFatherLevel = savedlastBreakFatherLevel;
        return s;
    }

    public Stmt casestmts() throws IOException {
        Stmt s = Stmt.Null;
        
        while(look.tag != Tag.CASE && look.tag != '}' && look.tag != Tag.DEFAULT){
            s = new Seq(s,stmt());
        }
        return s;
    }

    public Stmt casestmt() throws IOException {
        Env savedEnv = top;
        boolean savedHasDecl = hasDecl;
        hasDecl = false;                
        Stmt s = casestmts();
        if(hasDecl){
            s = new Seq(s,Stmt.RecoverStack);
            nowLevel --;
            top = savedEnv;
        }
        hasDecl = savedHasDecl;
        return ENABLE_STMT_OPT?s.optimize():s;
    }

    public Stmt switchstmt() throws IOException {
        match(Tag.SWITCH);
        match('(');
        //Available types for switch
        //string,int,char
        Expr condition = expr();
        Switch sw = Switch.getSwitch(condition);
        lastBreakFatherLevel = nowLevel;
        Stmt.BreakEnclosing = sw;
        match(')');
        match('{');
        while(look.tag != '}'){
            if(check(Tag.CASE)) {
                Expr c = expr().optimize();
                if(!( c instanceof Constant ) ){
                    error("case expression `" + c + "' is not constant");
                }

                Constant val = (Constant) c;

                if(sw.isCaseSet(val)){
                    error("case `" + c + "' has been handled before");
                }
 
                match(':');

                sw.appendCase(val,casestmt());
            } else if (check(Tag.DEFAULT)) {
                match(':');
                if(sw.isDefaultSet()){
                    error("default case reoccurs");
                }
                /*stmts should be different from the normal stmts*/
                sw.setDefault(casestmts());
            } else {
                error("wrong symbol found in switch:`"+ look + "'");
            }
        }
        match('}');
        return sw;
    }
    
    public Stmt closure() throws IOException {
        if(look.tag == '{'){
            return block();
        } else {
            Env savedEnv = top;
            boolean savedHasDecl = hasDecl;
            hasDecl = false;
            Stmt s = stmt();
            if(hasDecl){
                s = new Seq(s,Stmt.RecoverStack);
                nowLevel--;
                top = savedEnv;
            }
            hasDecl = savedHasDecl;
            return s;
        }
    }

    /*
     * We put for-loop is like:
     * 1.PushStack ;
     * 2.For-decl;
     * 3.condition;
     * 4.for-body
     * 5.end;
     * 6.RecoverStack;
     * if it breaks,it will go to 6
     * if it continues,it will goto 5
     */
    public Stmt forloop() throws IOException {
        For fornode = new For();
        Stmt.Enclosing = fornode;
        move();
        match('(');
        Stmt s1;
        Env savedTop = top;
        boolean hasdecl = false;
        if(look.tag == ';'){
            s1 = Stmt.Null;
        } else if( look.tag == Tag.BASIC) {
            hasdecl = true;
            top = new Env(top);
            s1 = fordecl();
            nowLevel++;
        } else {
            s1 = new ExprStmt(expr());
        }
        lastIterationLevel = nowLevel;
        lastBreakFatherLevel = nowLevel;
        match(';');
        Expr e2 = (look.tag == ';')?Constant.True:expr();
        match(';');
        Stmt s3 = (look.tag == ')')?Stmt.Null:new ExprStmt(expr());
        match(')');
        Stmt s = closure();
        top = savedTop;
        fornode.init(s1,e2,s3,s);
        if(hasdecl)
            nowLevel--;
        s = hasdecl?new Seq(Stmt.PushStack,new Seq(fornode,Stmt.RecoverStack)):fornode;
        return s;
    }

    Stmt fordecl() throws IOException {
        /*
         * for(int i = 0,j = 0;;)
         */
        Decls s = new Decls();
        Type p = type();
        Token tok;
        if(p == Type.Void){
            error("can't declare " + p.toString() + " type variable");
        }
        do{
            Expr e = null;
            tok = look;
            match(Tag.ID);
            if(top.containsVar(tok)){
                error("variable `" + tok.toString() + "' redefined here");
            }
            if( getType(tok) != null ){
                error("variable `" + tok + "' shadows a struct type");
            }
            top.put(tok,p);
            if(check('=')){
                e = expr();
            } else {
                if(p instanceof Struct){
                    ((Struct)p).setUsed(lex.line,lex.filename);
                }
            }
            s.addDecl(Decl.getDecl(tok,p,e));
        } while(check(','));
        
        return s;
    }

    public Decls decls() throws IOException{
        Decls s = new Decls();
        Expr  e = null;
        while( look.tag == Tag.BASIC){
            Type p = type();
            if(p == Type.Void){
                error("can't declare " + p.toString() + " type variable");
            }
            Token tok;
            do{
                e = null;
                tok = look;
                match(Tag.ID);
                if(top.containsVar(tok)){
                    error("variable `" + tok.toString() + "' redefined here");
                }

                if( getType(tok) != null ){
                    error("variable `" + tok + "' shadows a struct type");
                }
                top.put(tok,p);
                if(check('=')){
                    e = expr();
                } else {
                    if(p instanceof Struct){
                        ((Struct)p).setUsed(lex.line,lex.filename);
                    }
                }
                s.addDecl(Decl.getDecl(tok,p,e));
            } while(check(','));
            match(';');
        }
        return s;
    }    

    public Type type() throws IOException {
        if(look.tag == Tag.ID){
            Token t = getType(look);
            if(t == null){
                error("type `" + look + "' not found");
            }
            look = t;
        }

        Type p = (Type)look;
        match(Tag.BASIC);
        //[4][3][2] = array of [3][]int
        //[3][]int = array of []int
        //[]int   = array of int
        //row-wize
        if( look.tag == '[' ){
            if(p == Type.Void){
                error("type `" + p.toString() + "' can't be element type of array");
            }
            return arrtype(p);
        }
        return p;
    }

    public Array arrtype(Type of) throws IOException {
        int size = 0;
        match('[');
        if(look.tag != ']'){
            Token sz = look;
            match(Tag.NUM);
            size = ((Num)sz).value;
        }
        match(']');
        if( look.tag == '[' ){
            of = arrtype(of);
        }
        return new Array(of,size);
    }

    public Expr expr() throws IOException {
        Expr e = typecheck();
        return ENABLE_EXPR_OPT?e.optimize():e;
    }

    public Expr typecheck() throws IOException {
        Expr e = assign();
        while(look.tag == Tag.INSTOF){
            Token tok = copymove();
            Type t = type();
            e = new IsInstanceOf(tok,e,t);
        }
        return e;
    }
    
    public Expr assign() throws IOException {
        Expr l =  condition();
        while(look.tag == '=' || look.tag == Tag.ADDASS || look.tag == Tag.MINASS 
              || look.tag == Tag.MULTASS || look.tag == Tag.DIVASS || look.tag == Tag.MODASS){
            l = SetFactory.getSet(copymove(),l,condition());
        }
        return l;
    }

    public Expr condition() throws IOException {
        Expr e = bool();
        Token t = look;
        if(check('?')){
            Expr iftrue = typecheck();
            match(':');
            Expr iffalse = condition();
            e = new Condition(t,e,iftrue,iffalse);
        }
        return e;
    }

    public Expr bool() throws IOException {
       Expr l =  join();
       while(look.tag == Tag.OR){
            l = new Or( copymove() ,l,join());
       }
       return l;
    }

    public Expr join() throws IOException {
       Expr l =  equality();
       while(look.tag == Tag.AND){
            l = new And(copymove(),l,equality());
       }
       return l;
    }

    public Expr equality() throws IOException {
       Expr l =  rel();
       if(look.tag == Tag.EQ || look.tag == Tag.NE ){
            l = RelFactory.getRel( copymove(),l,rel());
       }
       return l;
    }

    public Expr rel() throws IOException {
       Expr l =  add();
       if(look.tag == Tag.GE || look.tag == Tag.LE
             ||look.tag == '<' || look.tag == '>'){
            l = RelFactory.getRel(copymove(),l,add());
       }
       return l;
    }

    public Expr add() throws IOException {
       Expr l =  mult();
       while(look.tag == '+' || look.tag == '-' ){
            l = ArithFactory.getArith(copymove(),l,mult());
       }
       return l;
    }

    public Expr mult() throws IOException {
       Expr l =  unary();
       while(look.tag == '*' || look.tag == '/' || look.tag == '%'){
            l = ArithFactory.getArith(copymove(),l,unary());
       }
       return l;
    }

    public Expr unary() throws IOException {
       switch(look.tag){
       case '!':
            return new Not(copymove(),unary());
       case Tag.SIZEOF:
            return new SizeOf(copymove(),unary());
       case Tag.NEW:
            Token l = copymove();
            match('<');
            Type  t = type();
            if(t == Type.Void){
                error("can't use type `" + t +"'");
            }
            match('>');
            match('(');
            Expr e = typecheck();
            match(')');
            return new NewArray(l,t,e);
       case '-':
       case Tag.INC:
       case Tag.DEC:
            return UnaryFactory.getUnary(copymove(),unary());
       default:
            return postinc();
       }
    }

    public Expr postinc() throws IOException {
        Expr e = postfix();
        switch(look.tag){
        case Tag.INC:
        case Tag.DEC:
            return PostUnaryFactory.getUnary(copymove(),e);
        default:
            return e;
        }
    }

    public Expr postfix() throws IOException {
       Expr e = factor();
       switch(look.tag){
       case '[':
       case '.':
            return access(e);
       default:
            return e;
       }
    }

    public Expr access(Expr e) throws IOException {
        do{
            if(look.tag == '.')
                e = member(e);
            else
                e = offset(e);
        }while(look.tag == '[' || look.tag == '.');
        return e;
    }

    public Expr member(Expr e) throws IOException {
        match('.');
        Token mname = look;
        match(Tag.ID);
        if(look.tag == '('){
           ArrayList<Expr>  p = parameters();
            if(!(e.type instanceof Struct))
                error("member function is for struct,not for `" + e.type +"'");
            Struct s = (Struct)(e.type);
            FunctionBasic f = s.getNormalFunction(mname);
            if(f == null){
                f = s.getVirtualFunction(mname);
                if(f == null)
                    error("member function `" + mname + "' not found");
                f_used.add(f);
                /*Pass `this' reference as the first argument*/
                return new VirtualFunctionInvoke(e,f,p);
            } else {
                f_used.add(f);
                /*Pass `this' reference as the first argument*/
                p.add(0,e);
                return new FunctionInvoke(f,p);
            } 
        }
        return new StructMemberAccess(e,mname);
    }

    public Expr offset(Expr e) throws IOException {
        match('[');
        /*check type*/
        Expr loc = expr();
        match(']');
        /*if it is string item access*/
        if(e.type == Type.Str){
            if(e instanceof Var){
                e = new StringVarAccess((Var)e,loc);
            } else {
                e = new StringAccess(e,loc);
            }
            return e;
        }

        if(!(e instanceof Var && e.type instanceof Array)){
            error("operand `[]` should be used for array variable or string,not for " + e.type);
        }

        Type t = ((Array)(e.type)).of;/*element type*/

        e = new ArrayVar((Var)e,t,loc);
        /*for string index access*/
        return e;
    }

    public Expr factor() throws IOException {
        Expr l,r;
        
        switch(look.tag){
        case Tag.ID:
            Token tmp = copymove();
            
            if(look.tag == '('){
                return function(tmp);
            }
            EnvEntry ee = top.get(tmp);
            if(ee == null){
                error("variable `" + tmp + "' not declared.");
            }
            /*
             * Level 0 is for the global variables
             * By default,use offset to present stack level to get var 
             * address in runtime stack<stackoffset,varoffset>
             * But for the global variable,we can't know what it is 
             * exactly in functions.So we use the AbsoluteVar<stackbackoffset,varoffset>.
             */
            return ee.stacklevel == 0? new AbsoluteVar(tmp,ee.type,0,ee.offset) : new Var(tmp,ee.type,top.level - ee.stacklevel,ee.offset);
        case Tag.TRUE:
            move();
            return Constant.True;
        case Tag.FALSE:
            move();
            return Constant.False;
        case Tag.NUM:
            return new Constant(copymove(),Type.Int);
        case Tag.STR:
            return new Constant(copymove(),Type.Str);
        case Tag.FLOAT:
            return new Constant(copymove(),Type.Real);
        case Tag.CHAR:
            return new Constant(copymove(),Type.Char);
        case Tag.BIGNUM:
            return new Constant(copymove(),Type.BigInt);
        case Tag.BIGFLOAT:
            return new Constant(copymove(),Type.BigReal);
        case '(':
            return cast();
        default:
            error("unexpected token found:" + look );
            return null;
        }
    }

    public Expr cast() throws IOException {
        Expr e = null;
        match('(');
        switch(look.tag){
        case Tag.ID:
            Type t1 = getType(look);
            /*check if it is just a variable*/
            if(t1 == null){
                e = typecheck();
                match(')');
                break;
            }
            look = t1;
        case Tag.BASIC:
            Type t = type();
            match(')');
            Expr f = unary();
            assert(f != null);
            e = f;
            if(!f.type.equals(t))
                e = ConversionFactory.getAutoDownCastConversion(f,t);
            if(e == null)
                error("can't convert " + f.type + " to " + t);
            break;
        default:
            e = typecheck();
            match(')');
            break;
        }
        return e;
    }

    public Expr function(Token id) throws IOException {
        FunctionBasic f = table.getFuncType(id);

        if(f == null) {
            error("function `" + id + "' not found.");
        }
        ArrayList<Expr> p = parameters();
        return new FunctionInvoke(f,p);
    }

    public ArrayList<Expr> parameters() throws IOException  {
        match('(');
        ArrayList<Expr> p = new ArrayList<Expr>();
        if(!check(')')){
            do{
                p.add(expr());
            }while(check(','));
            match(')');
        }
        return p;
    }

    public static void main(String[] args) throws Exception {
        Lexer l = new Lexer();
        l.open("test.txt");
        new Parser(l).program().run();
    }
}