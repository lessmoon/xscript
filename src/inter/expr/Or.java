package inter.expr;

import lexer.Token;

public class Or extends Logical{
    public Or(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }
    
    @Override
    public Value getValue(){
        return expr1.getValue() != Value.False? Value.True:expr2.getValue();
    }
}