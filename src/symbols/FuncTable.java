package symbols;

import lexer.*;

import java.util.*;

public class FuncTable {
    HashMap<Token,Type> table = new HashMap<Token,Type>(); 

    public FuncTable(){
        
    }
    
    public boolean addFunc(Token id,Type t){
        return table.put(id,t) != null;
    }
    
    public Type getFuncType(Token id){
        return table.get(id);
    }
}