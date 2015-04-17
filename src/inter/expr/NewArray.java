package inter;

import lexer.*;
import symbols.*;

public class NewArray extends Op {
    public Expr size;
    public NewArray(Token tok,Type of,Expr sz){
        super(tok,null);
        size = sz;
        type = new Array(of,0);
    }

    void check(){
        Type t = Type.max(Type.Int,size.type);
        if(t != Type.Int){
            error("Array size can't be `" + size.type + "'");
        }
        size = ConversionFactory.getConversion(size,Type.Int);
    }

    boolean isChangeable(){
        return true;
    }

    public Expr optimize(){
        if(isChangeable()){
            size = size.optimize();
            return this;
        } else {
            return getValue();
        }
    }

    public Constant getValue(){
        Constant v = size.getValue();
        return new ArrayConst((Array)type,((Num)(v.op)).value);
    }

    public String toString(){
        return "new []" + type.toString();
    }
}