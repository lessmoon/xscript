package symbols;

import java.util.*;import lexer.*; import inter.*;

public class Env {
    private HashMap<Token,Type> table;
    protected Env prev;
    public Env(Env n){
        table = new HashMap<Token,Type>();
        prev = n;
    }
    public void put(Token w,Type t){
        table.put(w,t);
    }
    public Type get(Token w){
        for(Env e = this; e != null; e = e.prev){
            Type found = e.table.get(w);
            if( found != null)
                return found;
        }
        return null;
    }
}