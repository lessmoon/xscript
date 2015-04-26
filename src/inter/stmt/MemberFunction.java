package inter.stmt;

import lexer.*;
import symbols.*;
import inter.util.Para;

import java.util.ArrayList;

public class MemberFunction extends Function {
    Struct stct;
    public MemberFunction(Token n,Type t,ArrayList<Para> p,Struct sn){
        super(n,t,p);
        stct = sn;
    }

    public MemberFunction(Token n,Type t,Stmt s,ArrayList<Para> p,Struct sn){
        super(n,t,s,p);
        stct = sn;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer(type.toString());
        sb.append( stct.lexeme + "." + name + "(");
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