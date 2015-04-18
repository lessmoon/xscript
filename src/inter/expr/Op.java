package inter.expr;

import lexer.*;
import symbols.*;

public abstract class Op extends Expr{
    public Op(Token tok,Type p){
        super(tok,p);
    }
}