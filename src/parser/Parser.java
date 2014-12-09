package parser;

import lexer.*;
import symbols.*;
import inter.*;

import java.io.*;
import java.util.ArrayList;

public class Parser{
    private Lexer lex;
    private Token look;
    Env top = null;
    FuncTable table = new FuncTable();
    Type returnType = Type.Int;
    boolean hasDecl = false;
    /*integer numbers standing for the stack level*/
    public int lastIterationLevel = -1;
    public int lastFunctionLevel = 0;
    public int nowLevel = 0;
    
    public  Parser(Lexer l) throws IOException{
        lex = l;
        move();
        table.addFunc(Word.print,Function.print);
        table.addFunc(Word.strlen,Function.strlen);
    }

    public void move() throws IOException{
        look = lex.scan();
    }

    public Token copymove() throws IOException {
        Token tmp = look;
        move();
        return tmp;
    }

    public void error(String s){
        throw new RuntimeException("near line " + lex.line + ":" + s);
    }

    public void match(int t) throws IOException{
        if(look.tag == t)
            move();
        else 
            error("syntax error");
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
                deffunc();
                break;
            default:
                s = new Seq(s,stmt());
                break;
            }
        }
        return s;
    }

    public void deffunc() throws IOException {
        match(Tag.DEF);
        Type savedType = returnType;
        ArrayList<Para> pl = new ArrayList<Para>();
        returnType = type();
        Token name = look;
        match(Tag.ID);
        match('(');
        Env savedEnv = top;
        top = new Env(top);
        boolean savedHasDecl = hasDecl;
        hasDecl = true;
        if(!check(')')){
            do{
                Type t = type();
                Token n = look;
                match(Tag.ID);
                pl.add(new Para(t,n));
                top.put(n,t);
            }while(check(','));
            match(')');
        }
        Function f = new Function(name,returnType,pl);
        table.addFunc(name,f);
        match('{');
        Stmt s = stmts();
        match('}');
        f.init(name,returnType,s,pl);
        top = savedEnv;
        returnType = savedType;
        hasDecl = savedHasDecl;
        return;
    }

    public Stmt block() throws IOException {
        match('{');
        Env savedEnv = top;
        boolean savedHasDecl = hasDecl;
        hasDecl = false;
        top = new Env(top);
        Stmt s = stmts();
        match('}');
        if(hasDecl)
            s = new Seq(Stmt.PushStack,new Seq(s,Stmt.RecoverStack));
        top = savedEnv;
        if(hasDecl)
            nowLevel--;
        hasDecl = savedHasDecl;
        return s;
    }

    public Stmt stmts() throws IOException {
        if(look.tag == '}') {
            return Stmt.Null;
        } else {
            return new Seq(stmt(),stmts());
        }
    }

    public Stmt stmt() throws IOException {
        Expr x;
        Stmt s,s1,s2;
        Stmt savedStmt;
        int savedLastIterationLevel = lastIterationLevel;

        switch(look.tag){
        case ';':
            move();
            return Stmt.Null;
        case Tag.IF:
            match(Tag.IF);
            match('(');
            x = expr();
            match(')');
            s1 = stmt();
            if(look.tag != Tag.ELSE) 
                return new If(x,s1);
            match(Tag.ELSE);
            s2 = stmt();
            return new Else(x,s1,s2);
        case Tag.WHILE:
            While whilenode = new While();
            savedStmt = Stmt.Enclosing;
            Stmt.Enclosing = whilenode;
            match(Tag.WHILE);
            match('(');
            x = expr();
            match(')');
            s1 = stmt();
            whilenode.init(x,s1);
            Stmt.Enclosing = savedStmt;
            lastIterationLevel = savedLastIterationLevel;
            return whilenode;
        case Tag.DO:
            Do donode = new Do();
            savedStmt = Stmt.Enclosing;
            Stmt.Enclosing = donode;
            match(Tag.DO);
            s1 = stmt();
            match(Tag.WHILE);
            match('(');
            x = expr();
            match(')');
            match(';');
            donode.init(s1,x);
            Stmt.Enclosing = savedStmt;
            lastIterationLevel = savedLastIterationLevel;
            return donode;
        case Tag.FOR:
            For fornode = new For();
            savedStmt = Stmt.Enclosing;
            Stmt.Enclosing = fornode;
            move();
            match('(');
            Expr e1 = expr();
            match(';');
            Expr e2 = expr();
            match(';');
            Expr e3 = expr();
            match(')');
            s = stmt();
            Stmt.Enclosing = savedStmt;
            fornode.init(e1,e2,e3,s);
            lastIterationLevel = savedLastIterationLevel;
            return fornode;
        case Tag.BREAK:
            match(Tag.BREAK);
            match(';');
            return new Break(nowLevel - lastIterationLevel);
        case Tag.RETURN:
            match(Tag.RETURN);
            Expr e = expr();
            match(';');
            return new Return(e,returnType,nowLevel - lastFunctionLevel);
        case '{':
            return block();
        case Tag.BASIC:
            if(!hasDecl)
                nowLevel ++;
            hasDecl = true;
            return decls();
        default:
            /*single expression statement*/
            s = new ExprStmt(expr());
            match(';');
            return s;
        }
    }

    public Decls decls() throws IOException{
        Decls s = new Decls();
        Expr  e = null;
        while( look.tag == Tag.BASIC){
            Type p = type();
            Token tok;
            do{
                e = null;
                tok = look;
                match(Tag.ID);
                top.put(tok,p);
                if(check('=')){
                    e = expr();
                }
                s.addDecl(new Decl(tok,p,e));
            } while(check(','));
            match(';');
        }
        return s;
    }    

    public Type type() throws IOException {
        Type p = (Type)look;
        match(Tag.BASIC);
        while( look.tag == '[' ){
            match('[');
            Token sz = look;
            match(Tag.NUM);
            p = new Array(p,((Num)sz).value);
            match(']');
        }
        return p;
    }

    public Expr expr() throws IOException {
        return assign().optimize();
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
            Expr iftrue = assign();
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
       case '-':
       case Tag.INC:
       case Tag.DEC:
            return UnaryFactory.getUnary(copymove(),unary());
       default:
            return postfix();
       }    
    }

    public Expr postfix() throws IOException {
       Expr e = factor();
       switch(look.tag){
       case Tag.INC:
       case Tag.DEC:
            return PostUnaryFactory.getUnary(copymove(),e);
       case '[':
            match('[');
            Expr i = assign();
            match(']');
            if(e instanceof Var){
                return new StringVarAccess((Var)e,i);
            }
            return new StringAccess(e,i);
       default:
            return e;
       }
    }

    public Expr factor() throws IOException {
        Expr l,r;
        
        switch(look.tag){
        case Tag.ID:
            Token tmp = copymove();
            if(look.tag == '(')
                return function(tmp);
            Type t = top.get(tmp);
            if(t == null){
                error("Variable " + tmp + " not declared.");
            }
            return new Var(tmp,t);
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
        case Tag.REAL:
            return new Constant(copymove(),Type.Float);
        case Tag.CHAR:
            return new Constant(copymove(),Type.Char);
        case '(':
            return cast();
        default:
            error("Unknown token found:" + copymove().tag);
            return null;
        }
    }

    public Expr cast() throws IOException {
        Expr e = null;
        match('(');
        switch(look.tag){
        case Tag.BASIC:
            Type t = (Type) copymove();
            match(')');
            Expr f = unary();
            assert(f != null);
            e = f;
            if(f.type != t)
                e = ConversionFactory.getConversion(f,t);
            if(e == null)
                error("Can't convert " + f.type + " to " + t);
            break;
        default:
            e = assign();
            match(')');
            break;
        }
        return e;
    }
    
    public Expr function(Token id) throws IOException {
        Function f = table.getFuncType(id);
        ArrayList<Expr> paras = new ArrayList<Expr>();
        if(f == null) {
            error("Function " + id + " not found.");
        }
        match('(');
        if(!check(')')){
            do{
                paras.add(assign());
            }while(check(','));
            match(')');
        }

        return new FunctionInvoke(f,paras);
    }
    
    public static void main(String[] args) throws Exception {
        new Parser(new Lexer()).program().run();
    }
}