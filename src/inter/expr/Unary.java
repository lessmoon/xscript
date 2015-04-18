package inter.expr;

import lexer.*;
import symbols.*;

public abstract class Unary extends Op{
    public Expr expr;
    public Unary(Token tok,Expr x){
        super(tok,null);
        expr = x;
        type = Type.max(Type.Int,expr.type);
    }

    boolean isChangeable(){
        return expr.isChangeable();
    }

    public Expr optimize(){
        if(isChangeable()){
            expr = expr.optimize();
            return this;
        } else {
            return getValue();
        }
    }
    
    public String toString(){
        return op.toString() + " " + expr.toString();
    }
}