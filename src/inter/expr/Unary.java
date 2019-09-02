package inter.expr;

import lexer.Token;

public abstract class Unary extends Op {
    public Expr expr;

    public Unary(Token tok, Expr x) {
        super(tok, x.type);
        expr = x;
        // type = Type.max(Type.Int,expr.type);
    }

    public boolean isChangeable() {
        return expr.isChangeable();
    }

    @Override
    public Expr optimize() {
        expr = expr.optimize();
        if (isChangeable()) {
            return this;
        } else {
            return getValue();
        }
    }

    public String toString() {
        return op.toString() + " " + expr.toString();
    }
}