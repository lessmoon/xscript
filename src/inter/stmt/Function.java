package inter.stmt;

import inter.util.Para;
import lexer.Token;
import symbols.Type;

import java.util.List;

public class Function extends FunctionBasic {
    public Stmt  stmt;
    
    public Function(Token n,Type t,List<Para> p){
        super(n,t,p);
        stmt = null;
    }

    public Function(Token n,Type t,Stmt s,List<Para> p){
        super(n,t,p);
        stmt = s;
    }

    public void init(Token n, Type t, Stmt s, List<Para> p){
        name = n;
        type = t;
        stmt = s;
        paralist = p;
    }

    public void run(){
        stmt.run();
    }
    
    @Override
    public boolean isCompleted(){
        return stmt != null;
    }
    
}