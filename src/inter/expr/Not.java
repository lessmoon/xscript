package inter.expr;

import lexer.Token;

public class Not extends Logical{
    public Not(Token tok,Expr x2){
        super(tok,x2,x2);
    }

    @Override
    public Constant getValue(){
        return expr1.getValue() != Constant.False? Constant.False:Constant.True;
    }

    @Override
    public String toString(){
        return op.toString() + " " + expr2.toString();
    }
}