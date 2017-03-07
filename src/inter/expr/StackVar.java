package inter.expr;

import lexer.Token;
import runtime.VarTable;
import symbols.Type;

public class StackVar extends Var {
    final int stacklevel;
    final int offset;
    public StackVar(Token w,Type t,int sl,int o){
        super(w,t);
        stacklevel = sl;
        offset = o;
    }

    @Override
    public boolean isChangeable(){
        return true;
    }

    @Override
    public Constant getValue(){
        return VarTable.getVar(stacklevel,offset);
    }
    
    public Constant setValue(Constant v){
        return  VarTable.setVar(stacklevel,offset,v);
    }

    @Override
    public String toString(){
        return "$$" + op + "["+stacklevel+","+offset+"]";
    }
}