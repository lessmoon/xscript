package inter.expr;

import lexer.BigFloat;
import lexer.BigNum;
import lexer.Char;
import lexer.Num;
import lexer.Tag;
import lexer.Token;
import symbols.Type;

import java.math.BigInteger;

class IntUnary extends Unary {
    IntUnary(Token tok, Expr x){
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

    @Override
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

class BigIntUnary extends IntUnary {
    BigIntUnary(Token tok, Expr x){
        super(tok,x);
    }

    @Override
    public Constant getValue(){
        Constant c = expr.getValue();
        switch(op.tag){
        case Tag.INC:
            return ((Var)expr).setValue(new Constant(((BigNum)(c.op)).value.add(BigInteger.ONE)));
        case Tag.DEC:
            return ((Var)expr).setValue(new Constant(((BigNum)(c.op)).value.subtract(BigInteger.ONE)));
        case '-':
            return new Constant(((BigNum)(c.op)).value.negate());
        default:
            error("Unknown operand:`" + op + "'");
            return null;
        }
    }
}

class RealUnary extends Unary {
    RealUnary(Token tok, Expr x){
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
    
    @Override
    public Constant getValue(){
        if(op.tag == '-'){
            Constant c = expr.getValue();
            return new Constant(-((lexer.Float)(c.op)).value);
        } else {
            error("Unknown operand:`" + op + "'");
            return null;
        }
    }
}

class BigRealUnary extends RealUnary {
    BigRealUnary(Token tok, Expr x){
        super(tok,x);
    }

    @Override
    public Constant getValue(){
        if(op.tag == '-'){
            Constant c = expr.getValue();
            return new Constant(((BigFloat)(c.op)).value.negate());
        } else {
            error("Unknown operand:`" + op + "'");
            return null;
        }
    } 
}

class CharUnary extends Unary {
    CharUnary(Token tok, Expr x){
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
        Constant c = expr.getValue();
        switch(op.tag){
        case Tag.INC:
            c = ((Var)expr).setValue(new Constant((char)(((Char)(c.op)).value + 1)));
            return c;
        case Tag.DEC:
            c = ((Var)expr).setValue(new Constant((char)(((Char)(c.op)).value - 1)));
            return c;
        case '-':
            return new Constant((char)(-((Char)(c.op)).value));
        default:
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
            } else if( Type.Real == e.type ){
                return new RealUnary(tok,e);
            } else if( Type.Char == e.type ){
                return new CharUnary(tok,e);
            } else if( Type.BigInt == e.type ){
                return new BigIntUnary(tok,e);
            } else if( Type.BigReal == e.type ){
                return new BigRealUnary(tok,e);
            }
        }
        e.error("Operand:`" + tok + "' is not permitted here");
        return null;
    }
}