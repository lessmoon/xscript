package inter.expr;

import lexer.*;
import lexer.Float;
import symbols.Type;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Value extends Expr {
    public Value(Token tok, Type p) {
        super(tok, p);
    }

    public Value(char c) {
        super(new Char(c), Type.Char);
    }

    public Value(int i) {
        super(new Num(i), Type.Int);
    }

    public Value(String s) {
        super(new Str(s), Type.Str);
    }

    public Value(float r) {
        super(new lexer.Float(r), Type.Real);
    }

    public Value(BigInteger bi) {
        super(new BigNum(bi), Type.BigInt);
    }

    public Value(BigDecimal bd) {
        super(new BigFloat(bd), Type.BigReal);
    }

    @Override
    public Value getValue() {
        return this;
    }

    public static Value valueOf(boolean b) {
        return b ? True : False;
    }

    @Override
    public boolean isChangeable() {
        return false;
    }

    public static final Value True = new Value(Word.True, Type.Bool), False = new Value(Word.False, Type.Bool);

    public static final Value Null = new Value(Word.Null, Type.Null);

    /**
     * cast the value to java value
     * 
     * @param t the target type class
     * @param   <T> the target Type
     * @return the value
     */
    @SuppressWarnings("unchecked")
    public <T> T valueAs(Class<T> t) {
        if (t == Integer.class) {
            return (T) Integer.valueOf(((Num) op).value);
        } else if (t == Boolean.class || t == boolean.class) {
            return (T) Boolean.valueOf(this == Value.True);
        } else if (t == String.class) {
            return (T) ((Str) op).value;
        } else if (t == java.lang.Float.class) {
            return (T) java.lang.Float.valueOf(((Float) op).value);
        } else if (t == Double.class) {
            return (T) java.lang.Double.valueOf(((Float) op).value);
        } else if (t == BigInteger.class) {
            return (T) ((BigNum) op).value;
        } else if (t == BigDecimal.class) {
            return (T) ((BigFloat) op).value;
        } else if (t == Character.class) {
            return (T) Character.valueOf(((Char) op).value);
        } else {
            throw new ClassCastException("cannot cast " + op.getClass() + " => " + t);
        }
    }

    @Override
    public String toString() {
        return op.toString();
    }
}