package inter.expr;

import lexer.Token;

public class Not extends Logical{
    public Not(Token tok,Expr x2){
        super(tok,x2,x2);
    }

    @Override
    public Value getValue(){
        return expr1.getValue() != Value.False? Value.False: Value.True;
    }

    @Override
    public String toString(){
        return op.toString() + " " + expr2.toString();
    }
}