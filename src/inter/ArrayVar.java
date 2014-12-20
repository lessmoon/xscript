package inter;

import runtime.*;
import lexer.*;
import symbols.*;

public class ArrayVar extends Var {
    Expr loc;
    Var array;
    public ArrayVar(Var arr,Type t,Expr l){
        super(Word.array,t);
        loc = l;
        array = arr;
    }

    boolean isChangeable(){
        return true;
    }

    public Constant getValue(){
        ArrayConst v = (ArrayConst)array.getValue();
        int l = ((Num)(loc.getValue()).op).value;
        return v.getElement(l);
    }

    public Constant setValue(Constant v){
        int l = ((Num)(loc.getValue()).op).value;
        ArrayConst var = (ArrayConst)array.getValue();
        var.setElement(l,v);
        return  v;
    }
}