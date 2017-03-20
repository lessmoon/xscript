package inter.expr;

import lexer.Token;
import symbols.Type;

public abstract class Rel extends Logical {
    public Rel(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }

    @Override
    public Type check(Type p1, Type p2){
        if(p1 == p2)
            return Type.Bool;
        else 
            return null;
    }
}
