package inter.expr;

import runtime.*;
import lexer.*;
import symbols.*;

public class ArrayVar extends Var {
    Expr loc;
    Var array;
    public ArrayVar(Var arr,Type t,Expr l){
        super(Word.array,t,arr.stacklevel,arr.offset);
        loc = l;
        array = arr;
    }

    boolean isChangeable(){
        return true;
    }

    public Constant getValue(){
        ArrayConst v = (ArrayConst)array.getValue();
        int l = ((Num)(loc.getValue()).op).value;
        if(l >= v.size || l < 0){
            error("Index " + l + " out of range( 0 ~ " + (v.size - 1) + " )");
        }
        return v.getElement(l);
    }

    public Constant setValue(Constant v){
        int l = ((Num)(loc.getValue()).op).value;
        ArrayConst var = (ArrayConst)array.getValue();
        if(l >= var.size || l < 0){
            error("Index " + l + " out of range( 0 ~ " + (var.size - 1) + " )");
        }
        var.setElement(l,v);
        return  v;
    }
}