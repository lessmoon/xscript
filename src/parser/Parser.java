package parser;

import inter.expr.*;
import inter.stmt.*;
import inter.util.Para;
import lexer.*;
import runtime.LoadFunc;
import runtime.LoadStruct;
import runtime.TypeTable;
import symbols.*;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.Set;

public class Parser implements TypeTable {
    private Lexer lex;
    private Token look;
    private Env top = new Env();
    private HashMap<Token,Type> typetable = new HashMap<>();
    private FuncTable table = new FuncTable();
    private HashSet<Token> forbiddenFunctionName = new HashSet<>();
    private Type returnType = Type.Int;
    private boolean hasDecl = true;
    private boolean enableWarning = false;
    /*integer numbers standing for the stack level*/

    
    private Struct dStruct = null;
    private boolean isInStructInitialFunctionDefinition = false;
    
    private int lastBreakFatherLevel = -1;
    private int lastIterationLevel = -1;
    /* 
     * The variable lastFunctionLevel is for the  
     * local function definition.
     * For now,it is just 0(which means global level).
     */
    private int lastFunctionLevel = 0;
    private int nowLevel = 0;
    private Set<FunctionBasic> fUsed = new HashSet<>();
    
    private final boolean ENABLE_EXPR_OPT ;
    private final boolean ENABLE_STMT_OPT ;
    private boolean PRINT_FUNC_TRANSLATE = false;

    public  Parser(Lexer l) throws IOException{
        this(l,false,false);
    }

    public  Parser(Lexer l,boolean expr_opt,boolean stmt_opt) throws IOException{
        lex = l;
        move();
        top.put(Word.args,new Array(Type.Str));
        ENABLE_EXPR_OPT = expr_opt;
        ENABLE_STMT_OPT = stmt_opt;
        predefinedForbiddenFunctionName();
    }

    private void predefinedForbiddenFunctionName(){
        forbiddenFunctionName.add(Word.This);
        forbiddenFunctionName.add(Word.Super);
    }
    
    private boolean isForbiddenFunctionName(Token name){
        return forbiddenFunctionName.contains(name);
    }

    public void enablePrintFuncTranslate(){
        PRINT_FUNC_TRANSLATE = true;
    }

    public void disablePrintFuncTranslate(){
        PRINT_FUNC_TRANSLATE = false;
    }

    private void checkNamespace(Token name, String info){
        if( top.get(name) != null ){
            error(info + " `" + name + "' has a same name with a variable");
        }

        if( getType(name) != null ){
            error(info + " `" + name + "' has a same name with a struct");
        }
    }

    private void move() throws IOException {
        look = lex.scan();
    }

    private Token copymove() throws IOException {
        Token tmp = look;
        move();
        return tmp;
    }

    public void error(String s) throws RuntimeException{
        error(s,Lexer.line,Lexer.filename);
    }
    
    public void error(String s,int l,String f) throws RuntimeException{
        throw new RuntimeException("line " + l + " in file `" +  f + "':\n\t" + s);
    }
    
    public void warning(String s){
        warning(s,Lexer.line,Lexer.filename);
    }

    private void warning(String s, int l, String f){
        if(enableWarning)
            System.err.println("line " + l + " in file `" +  f + "':\n\t" + s);
    }

    private void match(char t) throws IOException{
        if(look.tag == t)
            move();
        else {
            if(look.tag == -1){
                error("unexpected end of file");
            }
            error("syntax error:expect `" + t +"',but found `" + look + "'");
        }
    }
        
    private void match(int t) throws IOException{
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
            case Tag.NATIVE:
                loadNative();
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

        table.getAllFunctions().stream().filter(b -> b.used() && !b.isCompleted()).forEach(b -> {
            error("function `" + b + "' used but not completed ", b.lexline, b.filename);
        });

        fUsed.stream().filter(b -> b.used() && !b.isCompleted()).forEach(b -> {
            error("function `" + b + "' used but not completed ", b.lexline, b.filename);
        });
        
        /*
         * check if there is a struct that is pure virtual but declared
         */
        for (Entry<Token, Type> info : typetable.entrySet()) {
            Struct st = (Struct) (info.getValue());
            if (st.used() && !st.isCompleted()) {
                error("`" + info.getValue() + "' used but not completed ", st.getFirstUsedLine(), st.getFirstUsedFile());
            }
        }
    }

    private void importLib() throws IOException {
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
    
    private void loadNative() throws IOException {
        List<Para> pl;
        match(Tag.NATIVE);
        StringBuilder sb = new StringBuilder();
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
            String clazzName = null;
            if(look.tag == Tag.STR){
                clazzName = look.toString();
                move();
                match(':');
            }

            if(check(Tag.STRUCT)){
                /*
                 * load struct
                 */
                Token name = look;
                match(Tag.ID);
                if(clazzName == null){
                    clazzName = name.toString();
                }
                Struct s = LoadStruct.loadStruct(sb.toString(),clazzName,name,this.lex,this);
                if(s == null){
                    error("incomplete extension struct:`" + name + "'");
                }
                checkNamespace(s.getName(),"struct");
                defType(s.getName(),s);

                if(check(':')){
                    Token base = look;
                    match(Tag.ID);
                    Type b = typetable.get(base);
                    if(b == null){
                        error("base struct `" + base + "' not found");
                    }
                    if(s.getFather() != b){
                        if(s.getFather() == null){
                            error("native struct `" + s.getName() + "' has no father");
                        } else {
                            error("native struct `" + s.getName() + "' has a different father (`"+ s.getFather() +"') with declaration here(`" + b+ "')");
                        }
                    }
                }

                if(check('{')){
                    /*
                     * Declaration of the the struct
                     * It does't check now by ignoring all character util '}'
                     */
                    while(!check('}')){
                        move();
                    }
                }
            } else {
                pl = new ArrayList<>();

                Type t = type();
                Token name = look;
                match(Tag.ID);
                if(clazzName == null){
                    clazzName = name.toString();
                }
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
                    f = LoadFunc.loadFunc(t,sb.toString(),clazzName,name,pl,this.lex,this);
                } catch (Exception e){
                    error("failed to load extension function `" + sb.toString()  + "." + clazzName + "'");
                }
                if(!table.addFunc(name,f)){
                    error("function name has conflict:" + name);
                }
            }
            match(';');
        }
    }

    private Type defType(Token tok, Type t) {
        return typetable.put(tok,t);
    }
    
    public Type getType(Token tok){
        return typetable.get(tok);
    }
    
    private void defStruct() throws IOException {
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
        
        dStruct = s;

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
                    error("can't overloading array type `" + t + "' casting operand");
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
                Token m;
                do {
                    m = look;
                    match(Tag.ID);
                    if(s.addMemberVariable(m,t) != null){
                        error("member `" + m.toString() + "' defined previously ");
                    }
                }while(check(','));
                match(';');
            }
        }
        
        dStruct = null;
    }

    /*
     * def a function in struct
     * s is the struct,op is the operand to overload(null for non-overloading)
     */
    private void defInnerStructFunction(Struct s, Token op) throws IOException{
        Env savedEnv = top;
        top = new Env(top);
        boolean savedHasDecl = hasDecl;
        hasDecl = true;
        Type savedType = returnType;
        isInStructInitialFunctionDefinition = (look == Word.This);

        Function f = (isInStructInitialFunctionDefinition)?initialFunctionDeclaration(s,op):innerFunctionDeclaration(s,op);

        if(!check(';')){
            match('{');
            Stmt stmt = stmts();
            match('}');
            f.init(f.name,f.type,stmt,f.paralist);
        }
        isInStructInitialFunctionDefinition = false;
        top = savedEnv;
        returnType = savedType;
        hasDecl = savedHasDecl;
    }

    /*
     * def this(){
     *      
     * }
     */
    private Function initialFunctionDeclaration(Struct s, Token op) throws IOException{
        if(op != null){
            error("initial function can't be overloaded");
        }
        top.put(Word.This,s);
        match(Tag.ID);
        List<Para> l = arguments();
        l.add(0,new Para(s,Word.This));
        Function f = new InitialFunction(Word.This,l,s);
        s.defineInitialFunction(f);
        return f;
    }
    
    private Function innerFunctionDeclaration(Struct s, Token op) throws IOException{
        int flag = 0;
        
        if(check(Tag.VIRTUAL)){
            flag = Tag.VIRTUAL;
        } else if(check(Tag.OVERRIDE)){
            flag = Tag.OVERRIDE;
        }
        
        
        returnType = type();
        Token fname = look;
        match(Tag.ID);
        if( isForbiddenFunctionName(fname) ){
            error("Function name can't be `" + fname + "'");
        }

        /*pass `this' reference as the first argument*/
        top.put(Word.This,s);
        List<Para> l = arguments();
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

        return f;
    }

    private void defFunction() throws IOException {
        match(Tag.DEF);
        Env savedEnv = top;
        top = new Env(top);
        boolean savedHasDecl = hasDecl;
        hasDecl = true;
        Type savedType = returnType;
        returnType = type();
        if(check('.')){//maybe the init function for struct defintion
            if(! (returnType instanceof Struct )){
                error("Struct name needed here,but found `" + returnType +"'");
            }
            Struct s = (Struct)returnType;
            Token name = look;
            match(Tag.ID);
            isInStructInitialFunctionDefinition = true;
            defOutterStructInitialFunction(s,name);
            isInStructInitialFunctionDefinition = false;
            top = savedEnv;
            returnType = savedType;
            hasDecl = savedHasDecl;
            return;
        }
        
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
            defOuterStructFunction((Struct) t,name,returnType);
            top = savedEnv;
            returnType = savedType;
            hasDecl = savedHasDecl;
            return;
        }

        List<Para> l = arguments();
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
    }

    private void defOutterStructInitialFunction(Struct t, Token name) throws IOException{
        dStruct = t;
        if(name != Word.This){
            error("unknown function name found here:`" + t.getName() + "."   + name + "'");
        }
        InitialFunction f = (InitialFunction)t.getInitialFunction();
        if(f == null){
            error("initial function declaration `" + t.getName() + ".[init]' not found");
        }
        if(f.isCompleted()){
            error("initial function `" + t.getName() + ".[init]' redefined");
        }

        top.put(Word.This,t);
        List<Para> l = arguments();
        l.add(0,new Para(t,Word.This));
        if(l.size() != f.paralist.size()){
            error("parameters number of function `" + t.getName() + ".[init]' doesn't match with its former declaration:expect " + (f.paralist.size() - 1) + " but found " + (l.size() - 1));
        }       

        for(int i = 1;i < l.size();i++){
            if(!l.get(i).type.equals(f.paralist.get(i).type)){
                error("function `" + t.getName() + ".[init]' has different arguments["+ (i-1) +"] type `" + l.get(i).type + "' with its former declaration `" + f.paralist.get(i).type + "'");
            }
        }
        match('{');
        Stmt stmt = stmts();
        match('}');
        f.init(stmt);
        dStruct = null;
    }
    
    private void defOuterStructFunction(Struct t, Token name, Type returnType) throws IOException{
        dStruct = t;

        //TODO:check if it is normal or virtual function
        //     if it is definition of the constructor
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
        List<Para> l = arguments();
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

        dStruct = null;
    }

    private List<Para> arguments() throws IOException {
        List<Para> pl = new ArrayList<>();
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

    private Stmt block() throws IOException {
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

    private Stmt stmts() throws IOException {
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
            savedLastBreakFatherLevel = lastBreakFatherLevel;
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
            While whileNode = new While();
            Stmt.Enclosing = whileNode;
            Stmt.BreakEnclosing = whileNode;
            match(Tag.WHILE);
            match('(');
            x = expr();
            match(')');
            s1 = closure();
            whileNode.init(x,s1);
            s =  whileNode;
            break;
        case Tag.DO:
            lastIterationLevel = nowLevel;
            lastBreakFatherLevel = nowLevel;
            Do doNode = new Do();
            Stmt.Enclosing = doNode;
            Stmt.BreakEnclosing = doNode;
            match(Tag.DO);
            s1 = closure();
            match(Tag.WHILE);
            match('(');
            x = expr();
            match(')');
            match(';');
            doNode.init(s1,x);
            s =  doNode;
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
        lastBreakFatherLevel = savedLastBreakFatherLevel;
        return s;
    }

    private Stmt casestmts() throws IOException {
        Stmt s = Stmt.Null;
        
        while(look.tag != Tag.CASE && look.tag != '}' && look.tag != Tag.DEFAULT){
            s = new Seq(s,stmt());
        }
        return s;
    }

    private Stmt casestmt() throws IOException {
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

    private Stmt switchstmt() throws IOException {
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
    
    private Stmt closure() throws IOException {
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
    private Stmt forloop() throws IOException {
        For fornode = new For();
        Stmt.Enclosing = fornode;
        move();
        match('(');
        Stmt s1;
        Env savedTop = top;
        boolean hasdecl = false;
        if(look.tag == ';'){
            s1 = Stmt.Null;
        } else if( look.tag == Tag.BASIC || getType(look) != null) {
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

    private Stmt fordecl() throws IOException {
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
            }
            s.addDecl(Decl.getDecl(tok,p,e));
        } while(check(','));
        
        return s;
    }

    private Decls decls() throws IOException{
        Decls s = new Decls();
        Expr  e;
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
					//deal with the initialization of array
                    if(p instanceof Array && look.tag == '{'){
						e = initiallist((Array)p);
					} else {
						e = expr();
					}
                }
                s.addDecl(Decl.getDecl(tok,p,e));
            } while(check(','));
            match(';');
        }
        return s;
    }    

	private Expr initiallist(Array p) throws IOException {
		/*
		 * initlist -> {list}
		 * list -> element | list,element
		 * element -> Expr | initlist
		 * Constraint:in `Y -> Y,B',Type Y must match with B
		 */
		 match('{');
		 Expr e = list(p.of);
		 match('}');
		 return e;
	}
	
	public Expr list(Type p) throws IOException {
		Expr initseq = Constant.Null;
		
		List<Expr> init_list = new ArrayList<Expr>();
		if(look.tag != '}'){
			do{
				init_list.add(element(p));
			} while(check(','));
		}

		Expr arrdefine = new NewArray(p,p,new Constant(init_list.size()));
		InPipe in = new InPipe(arrdefine);
		OutPipe out = new OutPipe(in);
		if(!init_list.isEmpty()){
			initseq = new inter.expr.Set(Word.ass,new ArrayVar(out,p,new Constant(0)),init_list.get(0));
		}
		
		for(int i = 1 ; i < init_list.size();i++){
			initseq = new SeqExpr(Word.array,initseq,new inter.expr.Set(Word.ass,new ArrayVar(out,p,new Constant(i)),init_list.get(i)));
		}
		//for each initseq 
		return initseq == Constant.Null?arrdefine:new SeqExpr(Word.array,in,initseq);
	}

	private Expr element(Type p) throws IOException{
		Expr e = null;
		if(look.tag == '{'){//array of array
			//p must be array
			if(p instanceof Array){
				e = initiallist((Array)p);
			} else {
				//TODO:details
				error("too many dimensions than the array declaration");
			}
		} else {
			e = expr();
		}
		if(!e.type.equals(p)){
			e = ConversionFactory.getConversion(e,p);
		}
		if(e == null){//type error
			error("array init error:can't convert `" + e.type +"' to `" + p + "'");
		}
		return e;
	}
	
    /*
     * match single type (except for array)
     */
    private Type singletype() throws IOException {
        if(look.tag == Tag.ID){
            Token t = getType(look);
            if(t == null){
                error("type `" + look + "' not found");
            }
            look = t;
        }
        Type p = null;
        //System.err.println("LOOK " + look);
        if(look.tag == Tag.BASIC){
            p = (Type)copymove();
        } else {
            error("type name wanted here,but found `" + look +"' ");
        }
        return p;
    }
    
    public Type type() throws IOException {

        Type p = singletype();

        if( look.tag == '[' ){
            if(p == Type.Void){
                error("type `" + p.toString() + "' can't be element type of array");
            }
            return arrtype(p);
        }
        return p;
    }

    private Array arrtype(Type of) throws IOException {
        match('[');
        match(']');
        Array a = new Array(of);
        while(check('[')){
            match(']');
            a = new Array(a);
        }
        return a;
    }

    public Expr expr() throws IOException {
        Expr e = typecheck();
        return ENABLE_EXPR_OPT?e.optimize():e;
    }

    private Expr typecheck() throws IOException {
        Expr e = assign();
        while(look.tag == Tag.INSTOF){
            Token tok = copymove();
            Type t = type();
            e = new IsInstanceOf(tok,e,t);
        }
        return e;
    }
    
    private Expr assign() throws IOException {
        Expr l =  condition();
        while(look.tag == '=' || look.tag == Tag.ADDASS || look.tag == Tag.MINASS 
              || look.tag == Tag.MULTASS || look.tag == Tag.DIVASS || look.tag == Tag.MODASS){
            l = SetFactory.getSet(copymove(),l,condition());
        }
        return l;
    }

    private Expr condition() throws IOException {
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

    private Expr join() throws IOException {
       Expr l =  equality();
       while(look.tag == Tag.AND){
            l = new And(copymove(),l,equality());
       }
       return l;
    }

    private Expr equality() throws IOException {
       Expr l =  rel();
       if(look.tag == Tag.EQ || look.tag == Tag.NE ){
            l = RelFactory.getRel( copymove(),l,rel());
       }
       return l;
    }

    private Expr rel() throws IOException {
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

    private Expr mult() throws IOException {
       Expr l =  unary();
       while(look.tag == '*' || look.tag == '/' || look.tag == '%'){
            l = ArithFactory.getArith(copymove(),l,unary());
       }
       return l;
    }

    private Expr unary() throws IOException {
       switch(look.tag){
       case '!':
            return new Not(copymove(),unary());
       case Tag.SIZEOF:
            return new SizeOf(copymove(),unary());
       case '-':
       case Tag.INC:
       case Tag.DEC:
            return UnaryFactory.getUnary(copymove(),unary());
       default:
            return postinc();
       }
    }

    private Expr postinc() throws IOException {
        Expr e = postfix();
        switch(look.tag){
        case Tag.INC:
        case Tag.DEC:
            return PostUnaryFactory.getUnary(copymove(),e);
        default:
            return e;
        }
    }

    private Expr postfix() throws IOException {
       Expr e = factor();
       switch(look.tag){
       case '[':
       case '.':
            return access(e);
       default:
            return e;
       }
    }

    private Expr access(Expr e) throws IOException {
        do{
            if(look.tag == '.'){
                match('.');
                e = member(e);
            } else {
                e = offset(e);
            }
        }while(look.tag == '[' || look.tag == '.');
        return e;
    }

    private Expr member(Expr e) throws IOException {
        Token mname = look;
        match(Tag.ID);
        if(look.tag == '('){
            List<Expr>  p = parameters();
            if(!(e.type instanceof Struct))
                error("member function is for struct,not for `" + e.type +"'");
            Struct s = (Struct)(e.type);
            FunctionBasic f = s.getNormalFunction(mname);
            if(f == null){
                f = s.getVirtualFunction(mname);
                if(f == null)
                    error("member function `" + mname + "' not found");
                fUsed.add(f);
                /*Pass `this' reference as the first argument*/
                return new VirtualFunctionInvoke(e,f,p);
            } else {
                fUsed.add(f);
                /*Pass `this' reference as the first argument*/
                p.add(0,e);
                return new FunctionInvoke(f,p);
            } 
        }
        return new StructMemberAccess(e,mname);
    }

    private Expr offset(Expr e) throws IOException {
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

		/*
		 * update:remove the judge that e must be a variable
		 */
        if(!(e.type instanceof Array)){
            error("operand `[]` should be used for array type or string,not for " + e.type);
        }

        Type t = ((Array)(e.type)).of;/*element type*/

        e = new ArrayVar(e,t,loc);
        /*for string index access*/
        return e;
    }

    private Expr factor() throws IOException {
        Expr r;
        
        switch(look.tag){
        case Tag.ID:
            Token tmp = copymove();
            
            if(look.tag == '('){
                return function(tmp);
            }
            EnvEntry ee = top.get(tmp);
            if(ee == null){
                /*
                 * if it is in struct function definition
                 */
                //if((ee = top.get(Word.This)) != null ){
                //    Expr pthis = ee.stacklevel == 0? new AbsoluteVar(tmp,ee.type,0,ee.offset) : new Var(tmp,ee.type,top.level - ee.stacklevel,ee.offset);
                //    return member(pthis);
                //}
                error("variable `" + tmp + "' not declared.");
            }
            /*
             * Level 0 is for the global variables
             * By default,use offset to present stack level to get var 
             * address in runtime stack<stackoffset,varoffset>
             * But for the global variable,we can't know what it is 
             * exactly in functions.So we use the AbsoluteVar<stackbackoffset,varoffset>.
             */
            return ee.stacklevel == 0? new AbsoluteVar(tmp,ee.type,0,ee.offset) : new StackVar(tmp,ee.type,top.level - ee.stacklevel,ee.offset);
        case Tag.NULL:
            move();
            return Constant.Null;
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
        case Tag.NEW:
            Token l = copymove();
            //match('<');
            Type  t = singletype();
            if(t == Type.Void){
                error("can't use type `" + t +"'");
            }
            //match('>');
            if(check('[')){
                Expr e = null;
                do{
                    if(!check(']')){
                        e = typecheck();
                        match(']');
                        break;
                    }
                    t = new Array(t);
                } while(check('['));

                if(e == null){
                    error("unknown array allocation size:" + t);
                }
                
                return new NewArray(l,t,e);
            } else {// it is `new' struct 
                if(t instanceof Struct){
                    ((Struct)t).setUsed(Lexer.line, Lexer.filename);
                    Expr eNew = new New(l,(Struct)t);
                    if(look.tag == '('){//has initial function
                        List<Expr> pl = parameters();
                        InPipe ipNew = new InPipe(eNew);
                        eNew = ipNew;
                        Expr init = new OutPipe(ipNew);
                        pl.add(0,init);
                        FunctionBasic f = ((Struct)t).getInitialFunction();
                        if(f == null ){
                            error("`" + t + "' doesn't have an initial function");
                        }
                        return new SeqExpr(Word.This,eNew,new FunctionInvoke(f,pl));
                    } else {
                        if( null != ((Struct)t).getInitialFunction()){
                            error("`" + t + "' has a defined initial function");
                        }
                        return eNew;
                    }
                } else {
                    error("new " + t + " is not permitted:`" + t + "' is not a struct type");
                    return null;
                }
            }
		case '(':
            return cast();
        default:
            error("unexpected token found:" + look );
            return null;
        }
    }

    private Expr cast() throws IOException {
        Expr e;
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
        FunctionBasic f;
        List<Expr> p = parameters();
        if( id == Word.Super ){
            Struct fs;
            if(dStruct == null || !isInStructInitialFunctionDefinition){
                error("Keyword `" + id + "' can be only used in the struct initial function");
            }
            fs = dStruct.getFather();
            if( fs == null ){
                error("`" + dStruct + "' has no father");
            }
            f = fs.getInitialFunction();
            if(f == null){
                error("`" + fs + "'(father of `" + dStruct + "') has no declared initial function");
            }
            Expr pthis;EnvEntry ee;
            ee = top.get(Word.This);
            assert(ee != null);
            pthis = new StackVar(dStruct,ee.type,top.level - ee.stacklevel,ee.offset);
            p.add(0,pthis);
        } else {
            f = table.getFuncType(id);
            if(f == null) {
                error("function `" + id + "' not found.");
            }
        }
        return new FunctionInvoke(f,p);
    }

    private List<Expr> parameters() throws IOException  {
        match('(');
        List<Expr> p = new ArrayList<>();
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