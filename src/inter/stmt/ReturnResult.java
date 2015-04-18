package inter.stmt;

import inter.expr.Constant;

public class ReturnResult extends RuntimeException {
    public final Constant value;
    public ReturnResult(Constant v){
        value = v;
    }
}