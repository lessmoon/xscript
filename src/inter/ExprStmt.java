package inter;

public class ExprStmt extends Stmt {
    Expr e;
    public ExprStmt( Expr e ){
        this.e = e;
    }
    
    public void run(){
        e.getValue();
    }
    
    public String toString(){
        return e.toString() + "\n";
    }
}