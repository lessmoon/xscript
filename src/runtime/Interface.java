package runtime;

import inter.expr.StructConst;
import inter.expr.Constant;
import inter.stmt.FunctionBasic;
import inter.stmt.ReturnResult;
import lexer.Token;
import symbols.Position;
import symbols.Struct;
import symbols.VirtualTable;

import java.util.ArrayList;

public class Interface{
    static public Constant invokeNormalFunctionOfStruct(StructConst c,FunctionBasic f,ArrayList<Constant> para){
        Constant result =  f.type.getInitialValue();
        
        VarTable.pushTop();
        VarTable.pushVar(c);
        for(Constant p : para){
            VarTable.pushVar(p);
        }
        //RunStack.invokeFunction(lexline,filename,f);
        try {
            f.run();
        } catch(ReturnResult e){
            result =  e.value;
        }
        //RunStack.endInvokeFunction();
        VarTable.popTop();
        return result;        
    }
    
    static public Constant invokeVirualFunctionOfStruct(StructConst c,Position pos,ArrayList<Constant> para){
        VirtualTable vtable = c.getVirtualTable();
        FunctionBasic f = vtable.getVirtualFunction(pos);
        Constant result =  f.type.getInitialValue();

        VarTable.pushTop();
        VarTable.pushVar(c);
        for(Constant p : para){
            VarTable.pushVar(p);
        }

        //RunStack.invokeFunction(lexline,filename,f);
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