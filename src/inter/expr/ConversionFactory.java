package inter.expr;

import lexer.*;
import symbols.*;
import inter.stmt.FunctionBasic;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.ArrayList;

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
        return new Constant(BigDecimal.valueOf((double)((lexer.Float)(v.op)).value));
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
        return new Constant((int)((lexer.Float)(v.op)).value);
    }
}

class CharIntConversion extends Conversion{
    CharIntConversion(Expr e){
        super(e,Type.Int,Type.Int);
    }

    public Constant getValue(){
        Constant v = e.getValue();
        return new Constant((int)((Char)(v.op)).value);
    }
}

class IntCharConversion extends Conversion{
    IntCharConversion(Expr e){
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
        if(t == Type.Str && !(src.type instanceof Array)){
            return new StrConversion(src);
        } else
            return null;
    }
}

class ConversionFactoryFactory {
    private static final BigIntConversionFactory bigIntFactory = new BigIntConversionFactory();
    private static final IntConversionFactory intFactory = new IntConversionFactory();
    private static final BigRealConversionFactory bigRealFactory = new BigRealConversionFactory();
    private static final RealConversionFactory realFactory = new RealConversionFactory();
    private static final CharConversionFactory charFactory = new CharConversionFactory();
    private static final StrConversionFactory strFactory = new StrConversionFactory();
    private static final BoolConversionFactory boolFactory = new BoolConversionFactory();
    private static final OtherConversionFactory otherFactory = new OtherConversionFactory();
    
    static Factory getConversionFactory(Expr src){
        Type t = src.type;
        if(t == Type.Int)
            return intFactory;
        else if(t == Type.Char)
            return charFactory;
        else if(t == Type.Real)
            return realFactory;
        else if(t == Type.Str)
            return strFactory;
        else if(t == Type.Bool)
            return boolFactory;
        else if(t == Type.BigInt)
            return bigIntFactory;
        else if(t == Type.BigReal)
            return bigRealFactory;
        else
            return otherFactory;
    }
}

class UpperCastConversion extends Conversion{
    UpperCastConversion(Expr e,Struct tar){
        super(e,tar,tar);
        assert(e.type instanceof Struct);
    }

    @Override
    public Constant getValue(){
        Constant v = e.getValue();
        if(v != Constant.Null){
            assert(v.type instanceof Struct);
        }
        return v;
    }
    
    @Override
    public Expr optimize(){
        e = e.optimize();
        return e;
    }
}

class DownCastConversion extends Conversion {
    private final Struct tar;

    DownCastConversion(Expr e,Struct tar){
        super(e,tar,tar);
        assert(e.type instanceof Struct);
        this.tar = tar;
    }

    @Override
    public Constant getValue(){
        Constant v = e.getValue();
        assert(v.type instanceof Struct);
        /*runtime check if it is ok to downcast*/
        if(!tar.equals(v.type) && !((Struct)(v.type)).isChildOf(tar)){
            error("can't cast from `" + v.type + "' to `" + tar + "'");
        }
        return v;
    }
}

class NullConversion extends Conversion {
    NullConversion(Expr e,Type tar){
        super(e,tar,tar);
        assert(tar instanceof Array || tar instanceof Struct);
    }

    @Override
    boolean isChangeable(){
        return false;
    }

    @Override
    public Expr optimize(){
        return e;
    }

    @Override
    public Constant getValue(){
        assert(e == Constant.Null);
        return e.getValue();
    }
}

public class ConversionFactory {
    static public Expr getAutoDownCastConversion(Expr src,Type t){
       if(src.type instanceof Struct){
            /*
             * inherited struct judge
             */
            if(t instanceof Struct){
                if(((Struct)t).isChildOf((Struct)src.type)){//down-cast
                    return new DownCastConversion(src,(Struct)t);
                }
            }
       }
       return getConversion(src,t);
    }
    
    static public Expr getConversion(Expr src,Type t){
       /*
        * if it is struct may have conversion override
        */

        Expr c = null;

        if(src == Constant.Null){
            if(t instanceof Array || t instanceof Struct)
                return new NullConversion(src,t);
        } else if(src.type instanceof Struct){
            /*
             * inherited struct judge
             */
            if(t instanceof Struct){
                if(((Struct)src.type).isChildOf((Struct)t)){//up-cast
                    return new UpperCastConversion(src,(Struct)t);
                }
            }

            Token fname = ((Struct)(src.type)).getOverloading(t);
            if(fname != null){
                ArrayList<Expr> p = new ArrayList<>();
                FunctionBasic f = ((Struct)(src.type)).getNormalFunction(fname);
                if(f != null){
                    p.add(src);
                    c = new FunctionInvoke(f,p);
                }
                f = ((Struct)(src.type)).getVirtualFunction(fname);
                if(f != null){
                    c =  new VirtualFunctionInvoke(src,f,p);
                }
            }
       } else {
            Factory f = ConversionFactoryFactory.getConversionFactory(src);
            c = f.getConversion(src,t);
       }
       if(c == null){
            src.error("Can't convert `" + src.type + "' to `" + t.toString() +"'");
       }
       return c;
    }
}