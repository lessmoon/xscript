package symbols;

import lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class Env {
    private     Map<Token,EnvEntry> table;
    private final Env prev;
    public  final int level;
    public Env(Env n){
        prev  = n;
        level = n.level + 1;
        table = new HashMap<>();
    }
    
    public Env(){
        prev  = null;
        level = 0;
        table = new HashMap<>();
    }

    public EnvEntry put(Token w,Type t){
        return table.put(w,new EnvEntry(t,level,table.size()));
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