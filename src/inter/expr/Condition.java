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
        if(t.type.equals(type))
            iftrue = ConversionFactory.getConversion(t,type);
        if(f.type.equals(type))
            iffalse = ConversionFactory.getConversion(f,type);
    }
    
    @Override
    boolean isChangeable(){
        if(cond.isChangeable())
            return true;
        else {
            cond = cond.getValue();/*don't calculate twice*/
            return cond != Constant.False?iftrue.isChangeable():iffalse.isChangeable();
        }
    }

    @Override
    public Expr optimize(){
        cond = cond.optimize();
        iftrue = iftrue.optimize();
        iffalse = iffalse.optimize();
        if(isChangeable()){
            return this;
        } else {
            return getValue();
        }
    } 

    @Override
    public Constant getValue(){
        return cond.getValue() != Constant.False?iftrue.getValue():iffalse.getValue();
    }
	
	@Override
    public String toString(){
        return "((" + cond + ")?(" + iftrue + "):(" + iffalse + "))";
    }
}