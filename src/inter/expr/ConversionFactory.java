package inter.expr;

import lexer.*;
import symbols.*;

import java.math.BigInteger;
import java.math.BigDecimal;

class NoConversion extends Conversion{
    NoConversion(Expr e){
        super(e,null,e.type);
    }

    public Constant getValue(){
        return e.getValue();
    }
}

class StrConversion extends Conversion{
    StrConversion(Expr e){
        super(e,Type.Str,Type.Str);
    }

    @Override
    public Constant getValue(){
        return new Constant("" + e.getValue());
    }
}

/*
 * The conversion from Real to BigInt is equal to  
 * Real -> BigReal -> BigInt
 */

class BigIntIntConversion extends Conversion{
    BigIntIntConversion(Expr e){
        super(e,Type.Int,Type.Int);
    }

    @Override
    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant(((BigNum)(v.op)).value.intValue());
    }
}

class IntBigIntConversion extends Conversion{
    IntBigIntConversion(Expr e){
        super(e,Type.BigInt,Type.BigInt);
    }

    @Override
    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant(BigInteger.valueOf((long)((Num)(v.op)).value));
    }
}

class BigIntRealConversion extends Conversion{
    BigIntRealConversion(Expr e){
        super(e,Type.Real,Type.Real);
    }

    @Override
    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant(((BigNum)(v.op)).value.floatValue());
    }
}

class BigIntBigRealConversion extends Conversion{
    BigIntBigRealConversion(Expr e){
        super(e,Type.BigReal,Type.BigReal);
    }

    @Override
    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant(new BigDecimal(((BigNum)(v.op)).value));
    }
}

class BigRealBigIntConversion extends Conversion{
    BigRealBigIntConversion(Expr e){
        super(e,Type.BigInt,Type.BigInt);
    }

    @Override
    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant((((BigFloat)(v.op)).value).toBigInteger());
    }
}

class BigRealRealConversion extends Conversion{
    BigRealRealConversion(Expr e){
        super(e,Type.Real,Type.Real);
    }

    @Override
    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant((((BigFloat)(v.op)).value).floatValue());
    }
}

class RealBigRealConversion extends Conversion{
    RealBigRealConversion(Expr e){
        super(e,Type.BigReal,Type.BigReal);
    }
    
    @Override
    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant(BigDecimal.valueOf((double)((Real)(v.op)).value));
    }
}

/*
 * The conversion from BigReal to Int/Char is equal to  
 * BigReal -> BigInt -> Int/Char
 */

class IntRealConversion extends Conversion{
    IntRealConversion(Expr e){
        super(e,Type.Real,Type.Real);
    }

    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant((float)((Num)(v.op)).value);
    }
}

class RealIntConversion extends Conversion{
    RealIntConversion(Expr e){
        super(e,Type.Int,Type.Int);
    }

    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant((int)((Real)(v.op)).value);
    }
}

class CharIntConversion extends Conversion{
    public CharIntConversion(Expr e){
        super(e,Type.Int,Type.Int);
    }

    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant((int)((Char)(v.op)).value);
    }
}

class IntCharConversion extends Conversion{
    public IntCharConversion(Expr e){
        super(e,Type.Char,Type.Char);
    }

    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant((char)((Num)(v.op)).value);
    }
}

abstract class Factory {
    public abstract Conversion getConversion(Expr src,Type t);
}

class BigIntConversionFactory extends Factory {
    @Override
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.BigInt)
            return new NoConversion(src);
        else if(t == Type.Int)
            return new BigIntIntConversion(src);
        else if(t == Type.Char)
            return new IntCharConversion(new BigIntIntConversion(src));
        else if(t == Type.BigReal)
            return new BigIntBigRealConversion(src);
        else if(t == Type.Real)
            return new BigIntRealConversion(src);
        else if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class IntConversionFactory extends Factory {
    @Override
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.BigInt)
            return new IntBigIntConversion(src);
        else if(t == Type.Int)
            return new NoConversion(src);
        else if(t == Type.Char)
            return new IntCharConversion(src);
        else if(t == Type.BigReal)
            return new BigIntBigRealConversion(new IntBigIntConversion(src));
        else if(t == Type.Real)
            return new IntRealConversion(src);
        else if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class RealConversionFactory extends Factory {
    @Override
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.BigInt)
            return new BigRealBigIntConversion(new RealBigRealConversion(src));
        else if(t == Type.Int)
            return new RealIntConversion(src);
        else if(t == Type.Char)
            return new IntCharConversion(new RealIntConversion(src));
        else if(t == Type.BigReal)
            return new RealBigRealConversion(src);
        else if(t == Type.Real)
            return new NoConversion(src);
        else if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class BigRealConversionFactory extends Factory{
    @Override
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.BigInt)
            return new BigRealBigIntConversion(src);
        else if(t == Type.Int)
            return new BigIntIntConversion(new BigRealBigIntConversion(src));
        else if(t == Type.Char)
            return new IntCharConversion(new BigIntIntConversion(new BigRealBigIntConversion(src)));
        else if(t == Type.BigReal)
            return new NoConversion(src);
        else if(t == Type.Real)
            return new BigRealRealConversion(src);
        else if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class CharConversionFactory extends Factory {
    @Override
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.BigInt)
            return new IntBigIntConversion(new CharIntConversion(src));
        else if(t == Type.Int)
            return new CharIntConversion(src);
        else if(t == Type.Char)
            return new NoConversion(src);
        else if(t == Type.BigReal)
            return new BigIntBigRealConversion(new IntBigIntConversion(new CharIntConversion(src)));
        else if(t == Type.Real)
            return new IntRealConversion(new CharIntConversion(src));
        else if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class BoolConversionFactory extends Factory {
    @Override
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class StrConversionFactory extends Factory {
    @Override
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.Str)
            return new NoConversion(src);
        else
            return null;
    }
}

class OtherConversionFactory extends Factory {
    @Override
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class ConversionFactoryFactory {
    static final BigIntConversionFactory bintf = new BigIntConversionFactory();
    static final IntConversionFactory intf = new IntConversionFactory();
    static final BigRealConversionFactory brealf = new BigRealConversionFactory();
    static final RealConversionFactory realf = new RealConversionFactory();
    static final CharConversionFactory charf = new CharConversionFactory();
    static final StrConversionFactory strf = new StrConversionFactory();
    static final BoolConversionFactory boolf = new BoolConversionFactory();
    static final OtherConversionFactory otherf = new OtherConversionFactory();
    
    static Factory getConversionFactory(Expr src){
        Type t = src.type;
        if(t == Type.Int)
                return intf;
            else if(t == Type.Char)
                return charf;
            else if(t == Type.Real)
                return realf;
            else if(t == Type.Str)
                return strf;
            else if(t == Type.Bool)
                return boolf;
            else if(t == Type.BigInt)
                return bintf;
            else if(t == Type.BigReal)
                return brealf;
            else
                return otherf;
        }
}

public class ConversionFactory {
    static public Conversion getConversion(Expr src,Type t){
       Factory f = ConversionFactoryFactory.getConversionFactory(src);
       Conversion c = f.getConversion(src,t);
       if(c == null){
            src.error("Can't convert `" + src.type + "' to `" + t.toString() +"'");
       }
       return c;
    }
}