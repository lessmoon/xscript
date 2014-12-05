package inter;

import lexer.*;
import symbols.*;

public abstract class Conversion extends Expr {
    public Expr e;
    Conversion(Expr e,Token op,Type t){
        super(op,t);
        this.e = e;
    }
    public abstract Constant getValue();
}