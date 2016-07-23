package symbols;

import inter.stmt.FunctionBasic;
import lexer.Token;

import java.util.Collection;
import java.util.HashMap;

public class FuncTable {
    public HashMap<Token,FunctionBasic> table;

    public FuncTable(){
        table = new HashMap<>();
    }

    public boolean addFunc(Token id,FunctionBasic f){
        return table.put(id,f) == null;
    }
    
    public FunctionBasic getFuncType(Token id){
        return table.get(id);
    }
    public Collection<FunctionBasic> getAllFunctions(){
        return table.values();
    }
}