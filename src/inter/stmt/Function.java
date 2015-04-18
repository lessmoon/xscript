package inter.stmt;

import lexer.*;
import symbols.*;
import inter.util.Para;

import java.util.ArrayList;

public class Function extends FunctionBasic {
    public Stmt  stmt;
    
    public Function(Token n,Type t,ArrayList<Para> p){
        super(n,t,p);
        stmt = null;
    }

    public Function(Token n,Type t,Stmt s,ArrayList<Para> p){
        super(n,t,p);
        stmt = s;
    }

    public void init(Token n,Type t,Stmt s,ArrayList<Para> p){
        name = n;
        type = t;
        stmt = s;
        paralist = p;
    }
    
    public void run(){
        stmt.run();
        return;
    }
    
    public boolean isCompleted(){
        return stmt != null;
    }
    
}