package inter.expr;

import lexer.*;
import lexer.Float;
import symbols.Type;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Constant extends Expr{
    public Constant(Token tok,Type p){
        super(tok,p);
    }

    public Constant(char c){
        super(new Char(c),Type.Str);
    }

    public Constant(int i){
        super(new Num(i),Type.Int);
    }

    public Constant(String s){
        super(new Str(s),Type.Str);
    }

    public Constant(float r){
        super(new lexer.Float(r),Type.Real);
    }

    public Constant(BigInteger bi){
        super(new BigNum(bi),Type.BigInt);
    }
    
    public Constant(BigDecimal bd){
        super(new BigFloat(bd),Type.BigReal);
    }
    
    @Override
    public Constant getValue(){
        return this;
    }

    public static Constant valueOf(boolean b){
        return b?True:False;
    }

    @Override
    public boolean isChangeable(){
        return false;
    }

    public static final Constant
        True = new Constant(Word.True,Type.Bool),
        False = new Constant(Word.False,Type.Bool);

    public static final Constant
        Null = new Constant(Word.Null,Type.Null);

    /**
     *  cast
     * @param t
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public<T> T valueAs(Class<T> t){
        if(t == Integer.class){
            return (T)Integer.valueOf(((Num)op).value);
        } else if(t == String.class){
            return (T)((Str)op).value;
        } else if(t == java.lang.Float.class ){
            return (T) java.lang.Float.valueOf(((Float)op).value);
        } else if(t == Double.class) {
            return (T) java.lang.Double.valueOf(((Float)op).value);
        } else if(t == BigInteger.class){
            return (T) ((BigNum)op).value;
        } else if(t == BigDecimal.class){
            return (T) ((BigFloat)op).value;
        } else if(t == Character.class){
            return (T) Character.valueOf(((Char)op).value);
        } else {
            throw new ClassCastException("cannot cast " + op.getClass() + " => " + t);
        }
    }

    @Override
    public String toString(){
        return op.toString();
    }
}