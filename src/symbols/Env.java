package symbols;

import com.sun.istack.internal.NotNull;
import inter.expr.Constant;
import lexer.Token;

import java.util.HashMap;
import java.util.Map;

/*
 * TODO:Test const enventry
 */
public class Env {
    private       Map<Token,EnvEntry> table;
    private final Env prev;
    public        int level;
    public        int nextOffset;
    
    private Env(@NotNull Env n){
        this(n,true);
    }

    private Env(@NotNull Env n,boolean needPush){
        prev = n;
        level = needPush?n.level + 1 : n.level;
        table = new HashMap<>();
        nextOffset = 0;
    }
    
    public Env(){
        prev  = null;
        level = 0;
        table = new HashMap<>();
        nextOffset = 0;
    }

    public EnvEntry put(Token w,Type t){
        this.setPush();
        return table.put(w,new EnvEntry(t,level,nextOffset++));
    }
    
    public EnvEntry put(Token w,Constant c){
        return table.put(w,new EnvConstEntry(c));
    }

    public EnvEntry get(Token w){
        for(Env e = this; e != null; e = e.prev){
            EnvEntry found = e.table.get(w);
            if( found != null)
                return found;
        }
        return null;
    }

    /**
     * active this present stack frame(allow it to define new var)
     */
    public void setPush(){
        if(prev != null)
            this.level = prev.level + 1;
    }

    /**
     * @return new Env with stack-pushing which means the new stack defined new var
     */
    public Env pushEnv(){
        return new Env(this);
    }

    /**
     * @return new Env without new stack-pushing which means the new stack hasn't define new var
     */
    public Env getNewEnv(){
        return new Env(this,false);
    }

    /*
     * return if the top environment contains this variable(w) 
     */
    public boolean containsVar(Token w){
        return table.containsKey(w);
    }
}