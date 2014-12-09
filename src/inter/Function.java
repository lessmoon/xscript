package inter;

import lexer.*;
import symbols.*;

import java.util.ArrayList;

public class Function extends Stmt {
    public Type  type;
    public Token name;
    public Stmt  stmt;
    public ArrayList<Para> paralist;

    static {
        ArrayList<Para> paralist = new ArrayList<Para>();
        paralist.add(new Para(Type.Str,Word.print));
        print = new Function(Word.print,Type.Bool,Stmt.Null,paralist){
            public void run(){
                Constant v = runtime.VarTable.getTop().getVar(Word.print);
                System.out.print(v);
                throw new ReturnResult(Constant.False);
            }
        };
        paralist = new ArrayList<Para>();
        paralist.add(new Para(Type.Str,Word.strlen));
        strlen = new Function(Word.strlen,Type.Int,Stmt.Null,paralist){
            public void run(){
                Constant v = runtime.VarTable.getTop().getVar(Word.strlen);
                throw new ReturnResult(new Constant(v.toString().length()));
            }
        };
    }

    public static final Function print,strlen;

    public Function(Token name,Type t,ArrayList<Para> p){
        init(name,t,null,p);    
    }

    public void init(Token n,Type t,Stmt s,ArrayList<Para> p){
        name = n;
        type = t;
        stmt = s;
        paralist = p;
    }
    
    public Function(Token n,Type t,Stmt s,ArrayList<Para> p){
        init(n,t,s,p);
    }

    public Para getParaInfo(int i){
        return paralist.get(i);
    }

    public int getParaNumber(){
        return paralist.size();
    }
    
    public void run(){
        stmt.run();
        return;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer(type.toString());
        sb.append(" " + name + "(");
        int i = 0;
        if(i < paralist.size()){
            sb.append(paralist.get(i++).toString());
            while(i < paralist.size() ){
                sb.append(paralist.get(i++).toString());
            }
        }
        sb.append(")");
        return sb.toString();
    }
}