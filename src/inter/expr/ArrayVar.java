package inter.expr;

import runtime.*;
import lexer.*;
import symbols.*;

public class ArrayVar extends Var {
    Expr loc;
    Expr array;
    public ArrayVar(Expr arr,Type t,Expr l){
        super(Word.array,t);
        loc = l;
        array = arr;
		if(!(array.type instanceof Array)){
			error("`" + array + "' is not array type");
		}
    }

    @Override
    boolean isChangeable(){
        return true;
    }

    @Override
    public Expr optimize(){
        loc = loc.optimize();
		array = array.optimize();
        return this;
    }

    @Override
    public Constant getValue(){
        Constant c = array.getValue();
        if(c == Constant.Null){
            error("null pointer error:try to get member of a null array");
        }
        
        ArrayConst v = (ArrayConst)c;
        int l = ((Num)(loc.getValue()).op).value;
        if(l >= v.size || l < 0){
            error("Index " + l + " out of range( 0 ~ " + (v.size - 1) + " )");
        }
        return v.getElement(l);
    }

    @Override
    public Constant setValue(Constant v){
        Constant c = array.getValue();
        if(c == Constant.Null){
            error("null pointer error:try to set member of a null array");
        }
        int l = ((Num)(loc.getValue()).op).value;
        ArrayConst var = (ArrayConst)c;
        if(l >= var.size || l < 0){
            error("Index " + l + " out of range( 0 ~ " + (var.size - 1) + " )");
        }
        var.setElement(l,v);
        return  v;
    }

    @Override
    public String toString(){
        return array.toString() + "[" + loc.toString() + "]";
    }
    
}