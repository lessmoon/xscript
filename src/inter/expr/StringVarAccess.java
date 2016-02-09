package inter.expr;

import runtime.*;
import lexer.*;
import symbols.*;

public class StringVarAccess extends Var {
    Var array;
    Expr index;

    public StringVarAccess(Var a,Expr i){
        super(Word.array,Type.Char);
        array = a;
        index = i;
        check();
    }

    void check(){
        if( array.type != Type.Str )
            error("array access is only for " + Type.Str + " now");
        if( Type.max(Type.Int,index.type) != Type.Int ){
            error("type " + index.type + " is not valid for array");
        }
        if(index.type != Type.Int)
            index = ConversionFactory.getConversion(index,Type.Int);
    }
    
    @Override
    boolean isChangeable(){
        return true;
    }

    @Override
    public Expr optimize(){
        index = index.optimize();
        if(isChangeable()){
            return this;
        } else {
            return getValue();
        }
    }

    @Override
    public Constant getValue(){
        int i = ((Num)(index.getValue().op)).value;
        String str = ((Str)(array.getValue().op)).value;
        return new Constant(str.charAt(i));
    }

    @Override
    public Constant setValue(Constant c){
        int i = ((Num)(index.getValue().op)).value;
        String str = ((Str)(array.getValue().op)).value;
        StringBuffer sb = new StringBuffer(str);
        sb.setCharAt(i,((Char)(c.op)).value);
        return array.setValue(new Constant(sb.toString()));
    }
}