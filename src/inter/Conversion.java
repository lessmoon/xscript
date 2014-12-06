package inter;

import lexer.*;
import symbols.*;

public abstract class Conversion extends Expr {
    public Expr e;
    Conversion(Expr e,Token op,Type t){
        super(op,t);
        this.e = e;
    }
    
    boolean isChangeable(){
        return e.isChangeable();
    }
    
    public Expr optimaze(){
        if(isChangeable()){
            e = e.optimaze();
            return this;
        } else {
            return getValue();
        }
    }
    
    public abstract Constant getValue();
}