package inter.stmt;

import inter.expr.Expr;
import inter.code.*;

import java.util.ArrayList;

public class ExprStmt extends Stmt {
    Expr e;
    public ExprStmt( Expr e ){
        this.e = e;
    }
    
    @Override
    public void run(){
        e.getValue();
    }
    
    @Override
    public void emitCode(ArrayList<SerialCode> i){
        i.add(new ExprCode(e));
    }
    
    @Override
    public String toString(){
        return e.toString() + "\n";
    }
    
    
}