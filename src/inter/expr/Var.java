package inter.expr;

import lexer.*;
import symbols.*;

public abstract class Var extends Expr {
    public Var(Token w,Type t){
        super(w,t);
    }

    @Override
    boolean isChangeable(){
        return true;
    }

    @Override
    public abstract Constant getValue();
    
    public abstract Constant setValue(Constant v);

}