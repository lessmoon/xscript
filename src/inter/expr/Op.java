package inter.expr;

import lexer.Token;
import symbols.Type;

public abstract class Op extends Expr {
    public Op(Token tok, Type p) {
        super(tok, p);
    }
}