package inter.stmt;

import lexer.*;
import symbols.*;
import inter.util.Para;
import runtime.VarTable;
import runtime.RunEnv;
import inter.code.*;

import java.util.ArrayList;
 
public class ExFunction extends FunctionBasic implements SerialCode{
    public extension.Function func;
    public ArrayList<SerialCode> body = new ArrayList<SerialCode>();
    
    public ExFunction(Type t,Token n,ArrayList<Para> pl,extension.Function f){
        super(n,t,pl);
        func = f;
        this.emitCode(body);
    }

    @Override
    public void run(){
        throw new ReturnResult(func.run(VarTable.getTop()));
    }

    @Override
    public void serially_run(RunEnv r){
        r.setResult(func.run(VarTable.getTop()));
        r.functionReturn();
    }

    @Override
    public void emitCode(ArrayList<SerialCode> i){
        i.add(this);
    }

    @Override
    public boolean isCompleted(){
        return true;
    }

    @Override
    public ArrayList<SerialCode> getBody(){
        return body;
    }
}