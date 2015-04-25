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

    @Override
    public void emitCode(ArrayList<SerialCode> i){
        loc.emitCode(i);
        i.add(SerialCode.PushCode);
        array.emitCode(i);
        i.add(SerialCode.ArrayAccess);
    }

    @Override
    public void emitLeftCode(ArrayList<SerialCode> i){
        loc.emitCode(i);
        i.add(SerialCode.PushCode);
        array.emitCode(i);
        i.add(SerialCode.ArrayAccess);
    }

    
    @Override
    boolean isChangeable(){
        return true;
    }

    @Override
    public Expr optimize(){
        loc = loc.optimize();
        return this;
    }

    @Override
    public Constant getValue(){
        ArrayConst v = (ArrayConst)array.getValue();
        int l = ((Num)(loc.getValue()).op).value;
        if(l >= v.size || l < 0){
            error("Index " + l + " out of range( 0 ~ " + (v.size - 1) + " )");
        }
        return v.getElement(l);
    }

    @Override
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