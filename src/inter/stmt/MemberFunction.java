package inter.stmt;

import lexer.Token;
import inter.util.Para;
import symbols.Struct;
import symbols.Type;

import java.util.List;

public class MemberFunction extends Function {
    private Struct struct;
    public MemberFunction(Token n, Type t, List<Para> p, Struct sn){
        super(n,t,p);
        struct = sn;
    }

    public MemberFunction(Token n,Type t,Stmt s,List<Para> p,Struct sn){
        super(n,t,s,p);
        struct = sn;
    }

    public Struct getStruct(){
        return struct;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder(type.toString() + " " );
        sb.append(struct.lexeme).append(".").append(name).append("(");
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