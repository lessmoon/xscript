package inter.stmt;

import lexer.Token;
import inter.util.Para;
import symbols.Type;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionBasic extends Stmt {
    public Type  type;
    public Token name;
    public List<Para> paralist;
    private boolean used = false;
    
    public FunctionBasic(Token name,Type t,List<Para> p){
        init(name,t,p);    
    }

    public void init(Token n,Type t,List<Para> p){
        name = n;
        type = t;
        paralist = new ArrayList<>(p);
    }

    public Para getParaInfo(int i){
        return paralist.get(i);
    }

    public int getParaNumber(){
        return paralist.size();
    }
    
    public abstract void run();
    public abstract boolean isCompleted();
    public boolean isNotCompleted(){
        return !isCompleted();
    }

    public boolean used(){
        return used;
    }
    public void setUsed(){
        used = true;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(type.toString());
        sb.append(" ").append(name).append("(");
        int i = 0;
        if(i < paralist.size()){
            sb.append(paralist.get(i++).toString());
            while(i < paralist.size() ){
                sb.append(",");
                sb.append(paralist.get(i++).toString());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setName(Token name) {
        this.name = name;
    }

    public void setParalist(List<Para> paralist) {
        this.paralist = paralist;
    }
}