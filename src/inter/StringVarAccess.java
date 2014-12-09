package inter;

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
        index = ConversionFactory.getConversion(index,Type.Int);
    }
    
    boolean isChangeable(){
        return true;
    }
    
    public Expr optimize(){
        if(isChangeable()){
            index = index.optimize();
            return this;
        } else {
            return getValue();
        }
    }

    public Constant getValue(){
        int i = ((Num)(index.getValue().op)).value;
        String str = ((Str)(array.getValue().op)).value;
        return new Constant(str.charAt(i));
    }

    public Constant setValue(Constant c){
        int i = ((Num)(index.getValue().op)).value;
        String str = ((Str)(array.getValue().op)).value;
        StringBuffer sb = new StringBuffer(str);
        sb.setCharAt(i,((Char)(c.op)).value);
        return array.setValue(new Constant(sb.toString()));
    }
}