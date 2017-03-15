package inter.expr;

import lexer.Token;

public class And extends Logical {
    public And(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }
    
    @Override
    public Value getValue(){
        return expr1.getValue() != Value.False?expr2.getValue(): Value.False;
    }
}