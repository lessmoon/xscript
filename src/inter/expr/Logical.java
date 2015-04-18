package inter.expr;

import lexer.*;
import symbols.*;

public abstract class Logical extends Expr {
    public Expr expr1,expr2;
    Logical(Token tok,Expr x1,Expr x2){
        super(tok,null);
        expr1 = x1;
        expr2 = x2;
        type = check(expr1.type,expr2.type);
        if(type == null) 
            error("type error");
    }

    boolean isChangeable(){
        return expr1.isChangeable() || expr1.isChangeable();
    }

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
}