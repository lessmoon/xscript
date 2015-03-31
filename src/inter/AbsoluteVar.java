package inter;

import runtime.*;
import lexer.*;
import symbols.*;

public class AbsoluteVar extends Var {
    public AbsoluteVar(Token w,Type t,int l,int o){
        super(w,t,l,o);
    }

    boolean isChangeable(){
        return true;
    }

    public Constant getValue(){
        /*stack order:top to down*/
        return VarTable.getVarAbsolutely(stacklevel,offset);
    }

    public Constant setValue(Constant v){
        /*stack order:top to down*/
        return  VarTable.setVarAbsolutely(stacklevel,offset,v);
    }
    
    public String toString(){
        return "aVAR["+stacklevel+","+offset+"](" + op + ")"; 
    }
}