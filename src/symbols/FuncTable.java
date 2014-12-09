package symbols;

import lexer.*;
import inter.*;

import java.util.*;

public class FuncTable {
    HashMap<Token,Function> table = new HashMap<Token,Function>(); 

    public FuncTable(){
        
    }
    
    public boolean addFunc(Token id,Function f){
        return table.put(id,f) != null;
    }
    
    public Function getFuncType(Token id){
        return table.get(id);
    }
}