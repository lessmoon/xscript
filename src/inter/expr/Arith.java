package inter.expr;

import lexer.*;
import symbols.*;

public abstract class Arith extends Op {
    public Expr expr1,expr2;
    public Arith(Token tok,Expr x1,Expr x2){
        super(tok,null);
        expr1 = x1;
        expr2 = x2;
        type = Type.max(expr1.type,expr2.type);
        if(type == null)
            error("type error");
        if(!check())
            x1.error("Operand `" + tok + "' can't be used between " + x1.type + " and " + x2.type);
    }

    public abstract boolean check();
    
    public Expr optimize(){
        if(isChangeable()){
            expr1 = expr1.optimize();
            expr2 = expr2.optimize();
            return this;
        } else {
            return getValue();
        }
    }

    boolean isChangeable(){
        return expr1.isChangeable() || expr2.isChangeable();
    }
    
    public String toString(){
        return expr1.toString() + " " + op.toString() + " " + expr2.toString(); 
    }
}
