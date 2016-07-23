package inter.stmt;

import lexer.Token;
import inter.util.Para;
import symbols.Type;

import java.util.List;

public abstract class FunctionBasic extends Stmt {
    public Type  type;
    public Token name;
    public List<Para> paralist;
    private boolean hasused = false;
    
    public FunctionBasic(Token name,Type t,List<Para> p){
        init(name,t,p);    
    }

    public void init(Token n,Type t,List<Para> p){
        name = n;
        type = t;
        paralist = p;
    }

    public Para getParaInfo(int i){
        return paralist.get(i);
    }

    public int getParaNumber(){
        return paralist.size();
    }
    
    public abstract void run();
    public abstract boolean isCompleted();
    public boolean used(){
        return hasused;
    }
    public void setUsed(){
        hasused = true;
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


}