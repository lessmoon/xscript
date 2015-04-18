package inter.stmt;

import lexer.*;
import symbols.*;
import inter.util.Para;
import runtime.VarTable;

import java.util.ArrayList;
 
public class ExFunction extends FunctionBasic {
    public extension.Function func;
    public ExFunction(Type t,Token n,ArrayList<Para> pl,extension.Function f){
        super(n,t,pl);
        func = f;
    }

    @Override
    public void run(){
        throw new ReturnResult(func.run(VarTable.getTop()));
    }
    
    @Override
    public boolean isCompleted(){
        return true;
    }
}