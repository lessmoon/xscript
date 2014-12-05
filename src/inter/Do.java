package inter;

import symbols.*;

public class Do extends Stmt{
    Expr expr;Stmt stmt;
    public Do(){
        expr = null;stmt = null;
    }

    public void init(Stmt s,Expr x){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool)
            expr.error("boolean required in do");
    }
    
    public void run(){
        do{
            stmt.run();
        }while(expr.getValue() != Constant.False);
    }
}