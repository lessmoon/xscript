package inter.expr;

import runtime.*;
import lexer.*;
import symbols.*;
import inter.code.Serial;


public class AbsoluteVar extends Var implements SerialCode{
    public AbsoluteVar(Token w,Type t,int l,int o){
        super(w,t,l,o);
    }

    @Override
    public void emitCode(ArrayList<SerialCode> i){
        i.add(RightAbsoluteVarCode(l,o));
    }

    @Override
    public void emitLeftCode(ArrayList<SerialCode> i){
        i.add(new LeftAbsoluteVarCode(l,o));
    }

    @Override
    public void serially_run(RunEnv re){
        re.setResult(this.getValue());
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