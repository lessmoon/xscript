package inter.stmt;

import lexer.*;
import symbols.*;
import inter.util.Para;

import java.util.ArrayList;

public abstract class FunctionBasic extends Stmt {
    public Type  type;
    public Token name;
    public ArrayList<Para> paralist;
    private boolean hasused = false;
    
    public FunctionBasic(Token name,Type t,ArrayList<Para> p){
        init(name,t,p);    
    }

    public void init(Token n,Type t,ArrayList<Para> p){
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
    
    public String toString(){
        StringBuffer sb = new StringBuffer(type.toString());
        sb.append(" " + name + "(");
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