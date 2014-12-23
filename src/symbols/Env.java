package symbols;

import java.util.*;
import lexer.*;
import inter.*;

public class Env {
    private     HashMap<Token,EnvEntry> table = new HashMap<Token,EnvEntry>();
    protected   final Env prev;
    public      final int level;
    public Env(Env n){
        prev  = n;
        level = n.level + 1;
    }
    
    public Env(){
        prev  = null;
        level = 0;
    }

    public void put(Token w,Type t){
        table.put(w,new EnvEntry(t,level,table.size()));
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