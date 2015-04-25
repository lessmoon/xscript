package inter.code;

import runtime.RunEnv;
import inter.expr.Expr;

public class ExprCode implements SerialCode {
    Expr e;

    public ExprCode(Expr e){
        this.e = e;
    }

    @Override
    public void serially_run(RunEnv r){
        r.setResult(e.getValue(r));
    }

    @Override
    public String toString(){
        return e.toString() + "\n";
    }
}