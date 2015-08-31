package inter.stmt;

import lexer.*;
import symbols.*;
import inter.util.Para;

import java.util.ArrayList;

public class InitialFunction extends Function {
    Struct stct;
    public InitialFunction(Token n,ArrayList<Para> p,Struct sn){
        super(n,Type.Void,p);
        stct = sn;
    }

    public InitialFunction(Token n,Stmt s,ArrayList<Para> p,Struct sn){
        super(n,Type.Void,s,p);
        stct = sn;
    }

    public Struct getStruct(){
        return stct;
    }
    
    public void init(Stmt s){
        stmt = s;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer(stct.lexeme + ".[init](");
        int i = 1;
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