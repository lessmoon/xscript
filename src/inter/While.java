package inter;

import symbols.*;

public class While extends Stmt{
    Expr expr;
    Stmt stmt;
    
    public While(){
        expr = null;
        stmt = null;
    }

    public void init(Expr x,Stmt s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool)
            expr.error("boolean required in while");
    }

    public void run(){
        while(expr.getValue() != Constant.False){
            stmt.run();
        }
    }
}