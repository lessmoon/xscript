package inter;

import symbols.*;

public class Else extends Stmt {
    Expr expr;
    Stmt stmt1,stmt2;
    public Else(Expr x,Stmt s1,Stmt s2){
        expr = x;
        stmt1 = s1;
        stmt2 = s2;
        if( expr.type != Type.Bool)
            expr.error("boolean required in if");
    }
    
    public void run(){
        if(expr.getValue() != Constant.False){
            stmt1.run();
        } else {
            stmt2.run();
        }
    }
}