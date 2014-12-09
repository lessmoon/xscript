package inter;

import lexer.*;
import symbols.*;

class IntUnary extends Unary {
    public IntUnary(Token tok,Expr x){
        super(tok,x);
        check();
    }

    void check(){
        switch(op.tag){
        case Tag.INC:
        case Tag.DEC:
            if(!(expr instanceof Var))
                error("operand `" + op + "' should be used for variable");
            return;
        case '-':
            return ;
        default:
            error("Unknown operand:`" + op + "'");
            return;
        }
    }

    public Constant getValue(){
        Constant c = expr.getValue();
        switch(op.tag){
        case Tag.INC:
            return ((Var)expr).setValue(new Constant(((Num)(c.op)).value + 1));
        case Tag.DEC:
            return ((Var)expr).setValue(new Constant(((Num)(c.op)).value - 1));
        case '-':
            return new Constant(-((Num)(c.op)).value);
        default:
            error("Unknown operand:`" + op + "'");
            return null;
        }
    }
}

class RealUnary extends Unary {
    public RealUnary(Token tok,Expr x){
        super(tok,x);
        check();
    }
    
    void check(){
        switch(op.tag){
        case '-':
            return;
        default:
            error("Operand:`" + op + "' is not permitted here");
            return;
        }
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

class CharUnary extends Unary {
    public CharUnary(Token tok,Expr x){
        super(tok,x);
        check();
    }
    
    void check(){
        switch(op.tag){
        case Tag.INC:
        case Tag.DEC:
            if(!(expr instanceof Var))
                error("operand `" + op + "' should be used for variable");
            return;
        case '-':
            return;
        default:
            error("Operand:`" + op + "' is not permitted here");
            return;
        }
    }
    
    public Constant getValue(){
        if(op.tag == '-'){
            Constant c = expr.getValue();
            return new Constant((char)(-((Char)(c.op)).value));
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
            } else if( Type.Char == e.type ){
                return new CharUnary(tok,e);
            }
        }
        return null;
    }
}