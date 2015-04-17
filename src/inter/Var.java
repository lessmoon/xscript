package inter;

import runtime.*;
import lexer.*;
import symbols.*;

public class Var extends Expr {
    final int stacklevel;
    final int offset;
    public Var(Token w,Type t,int sl,int o){
        super(w,t);
        stacklevel = sl;
        offset = o;
    }

    boolean isChangeable(){
        return true;
    }

    public Constant getValue(){
        return VarTable.getVar(stacklevel,offset);
    }

    public Constant setValue(Constant v){
        return  VarTable.setVar(stacklevel,offset,v);
    }

    public String toString(){
        return "VAR["+stacklevel+","+offset+"](" + op + ")"; 
    }
}