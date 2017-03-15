package inter.expr;

import lexer.*;
import symbols.Type;

import java.math.BigInteger;

class IntPostUnary extends Unary {
    public IntPostUnary(Token tok,Expr x){
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
        default:
            error("Unknown operand:`" + op + "'");
            return;
        }
    }

    public Value getValue(){
        Value c = expr.getValue();
        switch(op.tag){
        case Tag.INC:
            ((Var)expr).setValue(new Value(((Num)(c.op)).value + 1));
            return c;
        case Tag.DEC:
            ((Var)expr).setValue(new Value(((Num)(c.op)).value - 1));
            return c;
        default:
            error("Unknown operand:`" + op + "'");
            return null;
        }
    }
}

class BigIntPostUnary extends IntUnary {
    BigIntPostUnary(Token tok, Expr x){
        super(tok,x);
    }

    @Override
    public Value getValue(){
        Value c = expr.getValue();
        switch(op.tag){
        case Tag.INC:
             ((Var)expr).setValue(new Value(((BigNum)(c.op)).value.add(BigInteger.ONE)));
             return c;
        case Tag.DEC:
            ((Var)expr).setValue(new Value(((BigNum)(c.op)).value.subtract(BigInteger.ONE)));
            return c;
        default:
            error("Unknown operand:`" + op + "'");
            return null;
        }
    }
}

class CharPostUnary extends Unary {
    CharPostUnary(Token tok, Expr x){
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
        default:
            error("Unknown operand:`" + op + "'");
            return;
        }
    }

    public Value getValue(){
        Value c = expr.getValue();
        switch(op.tag){
        case Tag.INC:
            ((Var)expr).setValue(new Value((char)(((Char)(c.op)).value + 1)));
            return c;
        case Tag.DEC:
            ((Var)expr).setValue(new Value((char)(((Char)(c.op)).value - 1)));
            return c;
        default:
            error("Unknown operand:`" + op + "'");
            return null;
        }
    }
}

public class PostUnaryFactory {
    public static Unary getUnary(Token tok,Expr e){
        if( Type.numeric(e.type) ){
            if( Type.Int == e.type ){
                return new IntPostUnary(tok,e);
            } else if( Type.Char == e.type ){
                return new CharPostUnary(tok,e);
            } else if( Type.BigInt == e.type ){
                return new BigIntPostUnary(tok,e);
            }
        }

        e.error("Operand:`" + tok + "' is not permitted here");
        return null;
    }
}