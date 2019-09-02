package inter.stmt;

import inter.expr.Expr;
import vm.Compiler;

public class ExprStmt extends Stmt {
    private Expr e;

    public ExprStmt(Expr e) {
        this.e = e;
    }

    @Override
    public void run() {
        e.getValue();
    }

    @Override
    public String toString() {
        return e.toString() + "\n";
    }

    @Override
    public void compile(Compiler compiler) {
        //TODO: clear temp state
        e.compile(compiler);
    }
}