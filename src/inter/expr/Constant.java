package inter.expr;

import lexer.*;
import symbols.*;

import java.math.BigInteger;
import java.math.BigDecimal;

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

    @Override
    boolean isChangeable(){
        return false;
    }

    public static final Constant
        True = new Constant(Word.True,Type.Bool),
        False = new Constant(Word.False,Type.Bool);

    @Override
    public String toString(){
        return op.toString();
    }
}