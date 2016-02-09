package inter.expr;

import runtime.*;
import lexer.*;
import symbols.*;

public class AbsoluteVar extends StackVar {
    public AbsoluteVar(Token w,Type t,int l,int o){
        super(w,t,l,o);
    }

    @Override
    boolean isChangeable(){
        return true;
    }

    @Override
    public Constant getValue(){
        /*stack order:top to down*/
        return VarTable.getVarAbsolutely(stacklevel,offset);
    }

    @Override
    public Constant setValue(Constant v){
        /*stack order:top to down*/
        return  VarTable.setVarAbsolutely(stacklevel,offset,v);
    }
    
    @Override
    public String toString(){
        return "aVAR["+stacklevel+","+offset+"](" + op + ")"; 
    }
}