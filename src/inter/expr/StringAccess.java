package inter.expr;

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

        if(index.type != Type.Int)
            index = ConversionFactory.getConversion(index,Type.Int);
    }

    @Override
    boolean isChangeable(){
        return index.isChangeable() || array.isChangeable();
    }

    @Override
    public Expr optimize(){
        index = index.optimize();
        array = array.optimize();
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
}