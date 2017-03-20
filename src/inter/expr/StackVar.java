package inter.expr;

import lexer.Token;
import runtime.VarTable;
import symbols.Type;

public class StackVar extends Var {
    final int stackLevel;
    final int stackOffset;
    public StackVar(Token w,Type t,int sl,int o){
        super(w,t);
        stackLevel = sl;
        stackOffset = o;
    }

    @Override
    public boolean isChangeable(){
        return true;
    }

    @Override
    public Value getValue(){
        return VarTable.getVar(stackLevel, stackOffset);
    }
    
    public Value setValue(Value v){
        return  VarTable.setVar(stackLevel, stackOffset,v);
    }

    @Override
    public String toString(){
        return "$$" + op + "["+ stackLevel +","+ stackOffset +"]";
    }
}