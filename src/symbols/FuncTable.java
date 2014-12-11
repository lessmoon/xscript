package symbols;

import lexer.*;
import inter.*;

import java.util.*;

public class FuncTable {
    HashMap<Token,FunctionBasic> table = new HashMap<Token,FunctionBasic>(); 

    public FuncTable(){
        
    }
    
    public boolean addFunc(Token id,FunctionBasic f){
        return table.put(id,f) != null;
    }
    
    public FunctionBasic getFuncType(Token id){
        return table.get(id);
    }
}