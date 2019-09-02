package inter.expr;

import lexer.Token;
import symbols.Type;

public abstract class Conversion extends Expr {
    public Expr e;

    Conversion(Expr e, Token op, Type t) {
        super(op, t);
        this.e = e;
    }

    @Override
    public boolean isChangeable() {
        return e.isChangeable();
    }

    @Override
    public Expr optimize() {
        e = e.optimize();
        if (isChangeable()) {
            return this;
        } else {
            return getValue();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + e + ")";
    }

    public abstract Value getValue();
}