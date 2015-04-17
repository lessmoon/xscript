package inter;

import lexer.*;
import symbols.*;

public class StringAccess extends Expr {
    Expr array;
    Expr index;

    public StringAccess(Expr a,Expr i){
        super(Word.array,Type.Char);
        array = a;
        index = i;
        check();
    }
    
    void check(){
        if(array.type != Type.Str)
            error("array access is only for " + Type.Str + " now");
        if( Type.max(Type.Int,index.type) != Type.Int ){
            error("type " + index.type + " is not valid for array");
        }
        index = ConversionFactory.getConversion(index,Type.Int);
    }
    
    boolean isChangeable(){
        return index.isChangeable() || array.isChangeable();
    }
    
    public Expr optimize(){
        if(isChangeable()){
            index = index.optimize();
            array = array.optimize();
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
}