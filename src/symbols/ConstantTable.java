package symbols;

import inter.exper.Constant;
import lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class ConstantTable {
    private     Map<Token,Constant> table;
    private final ConstantTable prev;
    public  final int level;
    public ConstantTable(ConstantTable n){
        prev  = n;
        level = n.level + 1;
        table = new HashMap<>();
    }
    
    public ConstantTable(){
        prev  = null;
        level = 0;
        table = new HashMap<>();
    }

    public Constant put(Token w,Constant c){
        return table.put(w,c);
    }

    public EnvEntry get(Token w){
        for(Env e = this; e != null; e = e.prev){
            EnvEntry found = e.table.get(w);
            if( found != null)
                return found;
        }
        return null;
    }
    
    /*
     * return if the top environment contains this variable(w) 
     */
    public boolean containsVar(Token w){
        return table.containsKey(w);
    }
}