package inter;

import lexer.*;
import symbols.*;
import inter.Constant;
import inter.FunctionBasic;
import inter.Para;
import runtime.VarTable;

import java.util.ArrayList;
 
public class ExFunction extends FunctionBasic {
    public extension.Function func;
    public ExFunction(Type t,Token n,ArrayList<Para> pl,extension.Function f){
        super(n,t,pl);
        func = f;
    }

    public void run(){
        throw new ReturnResult(func.run(VarTable.getTop()));
    }
    
    public int getPosition(){
        return -1;/*not available*/
    }
}