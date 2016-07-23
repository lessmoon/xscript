package inter.expr;

import lexer.Token;
import symbols.Type;

public abstract class Arith extends Op {
    Expr expr1;
    Expr expr2;
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
    
    @Override
    public Expr optimize(){
        expr1 = expr1.optimize();
        expr2 = expr2.optimize();
        if(isChangeable()){
            return this;
        } else {
            return getValue();
        }
    }

    @Override
    boolean isChangeable(){
        return expr1.isChangeable() || expr2.isChangeable();
    }
    
    @Override
    public String toString(){
        return expr1.toString() + " " + op.toString() + " " + expr2.toString(); 
    }
}
