package inter;

import symbols.*;

public class If extends Stmt{
    Expr expr;
    Stmt stmt;
    public If(Expr x,Stmt s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool)
            expr.error("boolean requried in if");
    }

    public void run(){
        if(expr.getValue() != Constant.False){
            stmt.run();
        }
    }
}