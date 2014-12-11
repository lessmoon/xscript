package inter;

import runtime.*;
import lexer.*;
import symbols.*;

public class ArrayVar extends Var {
    Expr loc;
    public ArrayVar(Token w,Type t,Expr l){
        super(w,t);
        loc = l;
    }

    boolean isChangeable(){
        return true;
    }

    public Constant getValue(){
        ArrayConst v = (ArrayConst)VarTable.getTop().getVar(op);
        int l = ((Num)(loc.getValue()).op).value;
        return v.getElement(l);
    }

    public Constant setValue(Constant v){
        int l = ((Num)(loc.getValue()).op).value;
        ArrayConst var = (ArrayConst)VarTable.getTop().getVar(op);
        var.setElement(l,v);
        return  v;
    }
}