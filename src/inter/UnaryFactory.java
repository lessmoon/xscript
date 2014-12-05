package inter;

import lexer.*;
import symbols.*;

class IntUnary extends Unary {
    public IntUnary(Token tok,Expr x){
        super(tok,x);
    }

    public Constant getValue(){
        if(op.tag == '-'){
            Constant c = expr.getValue();
            return new Constant(-((Num)(c.op)).value);
        } else {
            error("Unknown operand:`" + op + "'");
            return null;
        }
    }
    
}

class RealUnary extends Unary {
    public RealUnary(Token tok,Expr x){
        super(tok,x);
    }
    
    public Constant getValue(){
        if(op.tag == '-'){
            Constant c = expr.getValue();
            return new Constant(-((Real)(c.op)).value);
        } else {
            error("Unknown operand:`" + op + "'");
            return null;
        }
    }
}

public class UnaryFactory {
    public static Unary getUnary(Token tok,Expr e){
        if( Type.numeric(e.type) ){
            if( Type.Int == e.type ){
                return new IntUnary(tok,e);
            } else if( Type.Float == e.type ){
                return new RealUnary(tok,e);
            }
        }
        return null;
    }
}