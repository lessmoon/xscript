package inter;

import runtime.*;
import lexer.*;
import symbols.*;

public class Var extends Expr {
    public Var(Token w,Type t){
        super(w,t);
    }

    boolean isChangeable(){
        return true;
    }

    public Constant getValue(){
        Constant v = VarTable.getTop().getVar(op);
        return v;
    }

    public Constant setValue(Constant v){
        return  VarTable.getTop().setVar(op,v);
    }
}