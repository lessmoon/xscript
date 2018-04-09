package inter.expr;

import lexer.Token;
import symbols.Type;

public class Condition extends Expr {
    private Expr cond;
    private Expr iftrue;
    private Expr iffalse;
    
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
        if(!t.type.isCongruentWith(type))
            iftrue = ConversionFactory.getConversion(t,type);
        if(!f.type.isCongruentWith(type))
            iffalse = ConversionFactory.getConversion(f,type);
    }
    
    @Override
    public boolean isChangeable(){
        if(cond.isChangeable())
            return true;
        else {
            cond = cond.getValue();/*don't calculate twice*/
            return cond != Value.False?iftrue.isChangeable():iffalse.isChangeable();
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
    public Value getValue(){
        return cond.getValue() != Value.False?iftrue.getValue():iffalse.getValue();
    }
    
    @Override
    public String toString(){
        return "((" + cond + ")?(" + iftrue + "):(" + iffalse + "))";
    }
}