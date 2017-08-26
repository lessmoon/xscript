package symbols;

import inter.expr.Value;
import lexer.Token;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Env {
    private     Map<Token,EnvEntry> table;

    public Env getPrev() {
        return prev;
    }

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

    public EnvEntry put(Token w, Type t, boolean isConst, Value init){
        return table.put(w,new EnvEntry(t,level,table.size(),isConst,init));
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

    public String toString(final int offset) {
        StringBuilder sb = new StringBuilder();
        for (Env x = this; x != null ; x = x.prev) {
            sb.append(x.level + offset).append(":{\n");
            String collect = x.table.entrySet().stream()
                    .sorted(Comparator.comparingInt(a -> a.getValue().offset))
                    .map(e -> "[" + e.getValue().offset + "]: " + e.getKey() + "<" + e.getValue().type + ">")
                    .collect(Collectors.joining(", "));
            sb.append(collect);
            sb.append("\n}\n");
        }

        return sb.toString();
    }
}