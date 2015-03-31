package inter;

import lexer.*;
import symbols.*;

import java.util.ArrayList;

public class Function extends FunctionBasic {
    public Stmt  stmt;

    public Function(Token n,Type t,ArrayList<Para> p){
        super(n,t,p);
        stmt = null;
    }

    public void init(Token n,Type t,Stmt s,ArrayList<Para> p){
        name = n;
        type = t;
        stmt = s;
        paralist = p;
    }
    
    public Function(Token n,Type t,Stmt s,ArrayList<Para> p){
        super(n,t,p);
        stmt = s;
    }

    public void run(){
        stmt.run();
        return;
    }
}