package parser;

import java.io.*;
import lexer.*;
import symbols.*;
import inter.*;

public class Parser{
    private Lexer lex;
    private Token look;
    Env top = null;
    public  Parser(Lexer l) throws IOException{
        lex = l;
        move();
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
        throw new Error("near line " + lex.line + ":" + s);
    }

    public void match(int t) throws IOException{
        if(look.tag == t)
            move();
        else 
            error("syntax error");
    }

    public Stmt block() throws IOException {
        match('{');
        Env savedEnv = top;
        top = new Env(top);
        Decls d = decls();
        Stmt s = stmts();
        match('}');
        if(d.size() > 0)
            s = new Seq(d,new Seq(s,Stmt.Recover));
        top = savedEnv;
        return s;
    }

    public Decls decls() throws IOException{
        Decls s = new Decls();
        while( look.tag == Tag.BASIC){
            Type p = type();
            Token tok = look;
            match(Tag.ID);
            match(';');
            top.put(tok,p);
            s.addDecl(new Decl(tok,p));
        }
        return s;
    }
    
    public Stmt stmts() throws IOException {
        if(look.tag == '}') {
            return Stmt.Null;
        } else 
            return new Seq(stmt(),stmts());
    }
 
    public Stmt stmt() throws IOException{
        Expr x;
        Stmt s,s1,s2;
        Stmt savedStmt;
        switch(look.tag){
        case ';':
            move();
            return Stmt.Null;
        case Tag.IF:
            match(Tag.IF);
            match('(');
            x = bool();
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
            x = bool();
            match(')');
            s1 = stmt();
            whilenode.init(x,s1);
            Stmt.Enclosing = savedStmt;
            return whilenode;
        case Tag.DO:
            Do donode = new Do();
            savedStmt = Stmt.Enclosing;
            Stmt.Enclosing = donode;
            match(Tag.DO);
            s1 = stmt();
            match(Tag.WHILE);
            match('(');
            x = bool();
            match(')');
            match(';');
            donode.init(s1,x);
            Stmt.Enclosing = savedStmt;
            return donode;
        case Tag.FOR:
            move();
            match('(');
            Expr e1 = expr();
            match(';');
            Expr e2 = expr();
            match(';');
            Expr e3 = expr();
            match(')');
            s = stmt();
            return new For(e1,e2,e3,s);
        case Tag.BREAK:
            match(Tag.BREAK);
            match(';');
            return new Break();
        case '{':
            return block();
        default:
            /*single expression statement*/
            s = new ExprStmt(expr());
            match(';');
            return s;
        }
    }    

    public Type type() throws IOException {
        Type p = (Type)look;
        match(Tag.BASIC);
        return p;
    }

    public Expr expr() throws IOException {
        return bool();
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
       while(look.tag == '*' || look.tag == '/' ){
            l = ArithFactory.getArith(copymove(),l,unary());
       }
       return l;
    }

    public Expr unary() throws IOException {
       if(look.tag == '!' ){
            return new Not(copymove(),unary());  
       } else if(look.tag == '-' ){
            return UnaryFactory.getUnary(copymove(),unary());
       } else
            return factor();
    }

    public Expr factor() throws IOException {
        Expr l,r;
        Token tmp = copymove();
        switch(tmp.tag){
        case Tag.ID:
            Var v = new Var(tmp,top.get(tmp));
            if( look.tag != '=' ){
                return v;
            }
            tmp = look;
            move();
            r = expr();
            if(r.type != v.type){
                l = ConversionFactory.getConversion(r,v.type);
                if(l == null){
                    error("Can't convert " + r.type + " to " +v.type);
                }
                r = l;
            }
            return new Set(tmp,v,r);
        case Tag.NUM:
            return new Constant(tmp,Type.Int);
        case Tag.STR:
            return new Constant(tmp,Type.Str);
        case Tag.REAL:
            return new Constant(tmp,Type.Float);
        case '(':
            l = expr();
            match(')');
            return l;
        default:
            error("Unknown token found:" + look.tag);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        new Parser(new Lexer()).stmt().run();
    }
}