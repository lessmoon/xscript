package inter.expr;

import lexer.*;
import symbols.*;

public abstract class Conversion extends Expr {
    public Expr e;
    Conversion(Expr e,Token op,Type t){
        super(op,t);
        this.e = e;
    }

    @Override
    boolean isChangeable(){
        return e.isChangeable();
    }

    @Override
    public Expr optimize(){
        e = e.optimize();
        if(isChangeable()){
            return this;
        } else {
            return getValue();
        }
    }

    @Override
    public String toString(){
        return getClass().getName() + "(" + e + ")";
    }
    
    public abstract Constant getValue();
}