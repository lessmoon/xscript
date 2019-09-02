package inter.stmt;

import inter.expr.Value;

public class ReturnResult extends RuntimeException {
    public final Value value;

    public ReturnResult(Value v) {
        value = v;
    }
}