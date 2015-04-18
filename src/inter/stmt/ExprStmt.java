package inter.stmt;

import inter.expr.Expr;

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
    public String toString(){
        return e.toString() + "\n";
    }
}