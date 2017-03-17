package runtime;

import inter.expr.StructValue;
import inter.expr.Value;
import inter.stmt.FunctionBasic;
import inter.stmt.ReturnResult;
import symbols.Position;
import symbols.VirtualTable;

import java.util.List;

public class Interface{
    static public Value invokeNormalFunctionOfStruct(StructValue c, FunctionBasic f, List<Value> para){
        Value result =  f.type.getInitialValue();
        
        VarTable.pushTop();
        VarTable.pushVar(c);
        for(Value p : para){
            VarTable.pushVar(p);
        }
        //RunStack.invokeFunction(line,filename,f);
        try {
            f.run();
        } catch(ReturnResult e){
            result =  e.value;
        }
        //RunStack.endInvokeFunction();
        VarTable.popTop();
        return result;        
    }
    
    static public Value invokeVirtualFunctionOfStruct(StructValue c, Position pos, List<Value> args){
        VirtualTable vtable = c.getVirtualTable();
        FunctionBasic f = vtable.getVirtualFunction(pos);
        Value result =  f.type.getInitialValue();

        VarTable.pushTop();
        VarTable.pushVar(c);
        for(Value p : args){
            VarTable.pushVar(p);
        }

        //RunStack.invokeFunction(line,filename,f);
        try {
            f.run();
        } catch(ReturnResult e){
            result =  e.value;
        }
        //RunStack.endInvokeFunction();
        VarTable.popTop();
        return result;
    }
}