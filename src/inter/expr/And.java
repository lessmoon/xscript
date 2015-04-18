package inter.expr;

import lexer.*;
import symbols.*;

public class And extends Logical {
    public And(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }
    
    @Override
    public Constant getValue(){
        return expr1.getValue() != Constant.False?expr2.getValue():Constant.False;
    }
}