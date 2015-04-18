package inter.expr;

import lexer.*;
import symbols.*;

public class Condition extends Expr {
    Expr cond;
    Expr iftrue;
    Expr iffalse;
    
    public Condition(Token tok,Expr c,Expr t,Expr f) {
        super(tok,null);
        cond = c;
        iftrue = t;
        iffalse = f;
        if(cond.type != Type.Bool)
            error("operand `:?' bool expression required in condition");
        type = Type.max(t.type,f.type);
        if(type == null){
            error("bad conversion between " + t.type + " and " + f.type);
        }
        if(t.type != type)
            iftrue = ConversionFactory.getConversion(t,type);
        if(f.type != type)
            iffalse = ConversionFactory.getConversion(f,type);
    }
    
    boolean isChangeable(){
        if(cond.isChangeable())
            return true;
        else {
            cond = cond.getValue();/*don't calculate twice*/
            return cond != Constant.False?iftrue.isChangeable():iffalse.isChangeable();       
        }
    }

    public Expr optimize(){
        if(isChangeable()){
            cond = cond.optimize();
            iftrue = iftrue.optimize();
            iffalse = iffalse.optimize();
            return this;
        } else {
            return getValue();
        }
    } 

    public Constant getValue(){
        return cond.getValue() != Constant.False?iftrue.getValue():iffalse.getValue();
    }
}