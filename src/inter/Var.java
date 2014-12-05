package inter;

import runtime.*;
import lexer.*;
import symbols.*;

public class Var extends Expr {
    public Var(Token w,Type t){
        super(w,t);
    }

    public Constant getValue(){
        Constant v = VarTable.getTop().getVar(op);
        System.out.println("get " + op + " = " + v );
        return v;
    }
    
    public Constant setValue(Constant v){
        return  VarTable.getTop().setVar(op,v);
    }
}