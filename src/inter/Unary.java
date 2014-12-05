package inter;

import lexer.*;
import symbols.*;

public abstract class Unary extends Op{
    public Expr expr;
    public Unary(Token tok,Expr x){
        super(tok,null);
        expr = x;
        type = Type.max(Type.Int,expr.type);
    }

    public String toString(){
        return op.toString() + " " + expr.toString();
    }
}