package inter.expr;

import lexer.Token;
import symbols.Type;

public abstract class Logical extends Expr {
    Expr expr1;
    Expr expr2;
    Logical(Token tok,Expr x1,Expr x2) {
        super(tok, null);
        expr1 = x1;
        expr2 = x2;
        type = check(expr1.type, expr2.type);
        if (type == null)
            error("type error:operand `" + tok + "' can't be used between `" + expr1.type + "' and `" + expr2.type + "'");
    }

    @Override
    public boolean isChangeable(){
        return expr1.isChangeable() || expr2.isChangeable();
    }

    @Override
    public Expr optimize(){
        if(isChangeable()){
            expr1 = expr1.optimize();
            expr2 = expr2.optimize();
            return this;
        } else {
            return getValue();
        }
    }

    public Type check(Type p1,Type p2){
        if(p1 == Type.Bool && p2 == Type.Bool)
            return Type.Bool;
        else 
            return null;
    }
	
	@Override
    public String toString(){
        return op.toString() + "(" + expr1.toString() + ","+ expr2.toString() + ")";
    }
}