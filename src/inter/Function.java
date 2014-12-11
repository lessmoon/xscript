package inter;

import lexer.*;
import symbols.*;

import java.util.ArrayList;

public class Function extends FunctionBasic {
    public Stmt  stmt;

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

    public Function(Token n,Type t,ArrayList<Para> p){
        super(n,t,p);
        stmt = null;
    }

    public void init(Token n,Type t,Stmt s,ArrayList<Para> p){
        name = n;
        type = t;
        stmt = s;
        paralist = p;
    }
    
    public Function(Token n,Type t,Stmt s,ArrayList<Para> p){
        super(n,t,p);
        stmt = s;
    }

    public void run(){
        stmt.run();
        return;
    }

}