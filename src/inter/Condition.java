package inter;

import lexer.*;
import symbols.*;
import gen.*;

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
            if(cond == Constant.True){
                return iftrue.optimize();
            } else if(cond == Constant.False){
                return iffalse.optimize();
            }
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

    public void emit(BinaryCodeGen bcg){
        Reference<Integer> after = new Reference<Integer>(),next = new Reference<Integer>();
        cond.emit(bcg);
        int i = bcg.getCurrentPosition();
        bcg.emit(CodeTag.JUMP_OP | CodeTag.JC_ZE<<CodeTag.OTHER_POSITION);
        bcg.emit(new IntegerSubCode(next,new Reference<Integer>(i)));
        iftrue.emit(bcg);
        i = bcg.getCurrentPosition();
        bcg.emit(CodeTag.JUMP_OP );
        bcg.emit(new IntegerSubCode(after,new Reference<Integer>(i)));
        next.setValue(bcg.getCurrentPosition());
        iffalse.emit(bcg);
        after.setValue(bcg.getCurrentPosition());
    }
}