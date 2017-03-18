package runtime;

import inter.expr.StructValue;
import inter.expr.Value;
import inter.stmt.FunctionBasic;
import inter.stmt.ReturnResult;
import symbols.Position;
import symbols.Type;
import symbols.VirtualTable;

import java.util.List;

public class Interface{
    static public Value invokeNormalFunctionOfStruct(StructValue c, FunctionBasic f, List<Value> args){
        Value result =  f.getType().getInitialValue();
        
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

    /**
     * Invoke virtual function of a struct
     * NOTE that the {@code args} doesn't include this reference
     * @param c the struct value
     * @param pos the virtual table position of this virtual function
     * @param args the args (except this reference)
     * @return the ret value or default return value for f.getType(){@linkplain Type#getInitialValue()}
     */
    static public Value invokeVirtualFunctionOfStruct(StructValue c, Position pos, List<Value> args){
        final VirtualTable vtable = c.getVirtualTable();
        final FunctionBasic f = vtable.getVirtualFunction(pos);
        Value result =  f.getType().getInitialValue();

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