package inter.stmt;

import lexer.*;
import symbols.*;
import inter.util.Para;
import inter.code.*;
import inter.expr.Expr;

import java.util.ArrayList;

public class Function extends FunctionBasic {
    public Stmt  stmt;
    public ArrayList<SerialCode> body = new ArrayList<SerialCode>();
    
    
    public Function(Token n,Type t,ArrayList<Para> p){
        super(n,t,p);
        stmt = null;
    }

    public Function(Token n,Type t,Stmt s,ArrayList<Para> p){
        super(n,t,p);
        stmt = s;
    }

    public void init(Token n,Type t,Stmt s,ArrayList<Para> p){
        name = n;
        type = t;
        stmt = s;
        paralist = p;
        if(stmt != null){
            this.emitCode(this.body);
        }
    }

    @Override
    public void run(){
        stmt.run();
        return;
    }
    
    @Override
    public void emitCode(ArrayList<SerialCode> i){
        //error("unexpected action");
        stmt.emitCode(i);
        i.add(new Return(Expr.VoidExpr,Type.Void,0));
    }

    @Override
    public ArrayList<SerialCode>  getBody(){
        return body;
    }

    @Override
    public boolean isCompleted(){
        return stmt != null;
    }
    
}