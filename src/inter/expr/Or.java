package inter.expr;

import lexer.*;
import symbols.*;

public class Or extends Logical{
    public Or(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }
    
    @Override
    public Constant getValue(){
        return expr1.getValue() != Constant.False?Constant.True:expr2.getValue();
    }
}