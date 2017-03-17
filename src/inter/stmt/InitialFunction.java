package inter.stmt;

import inter.util.Para;
import lexer.Token;
import symbols.Struct;
import symbols.Type;

import java.util.List;

public class InitialFunction extends Function {
    private Struct struct;
    public InitialFunction(Token n,List<Para> p,Struct sn){
        super(n,Type.Void,p);
        struct = sn;
    }

    public InitialFunction(Token n, Stmt s, List<Para> p, Struct sn){
        super(n,Type.Void,s,p);
        struct = sn;
    }

    public Struct getStruct(){
        return struct;
    }
    
    public void init(Stmt s){
        stmt = s;
    }

    @Override
    public String getDescription(boolean needStructInfo) {
        StringBuilder sb = new StringBuilder();
        if (needStructInfo) {
            sb.append(struct.lexeme).append(".");
        }
        sb.append("this(");
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