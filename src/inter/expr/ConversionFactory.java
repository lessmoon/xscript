package inter.expr;

import lexer.*;
import symbols.*;

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

    public Constant getValue(){
        return new Constant("" + e.getValue());
    }
}

class IntRealConversion extends Conversion{
    IntRealConversion(Expr e){
        super(e,Type.Float,Type.Float);
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

class IntConversionFactory extends Factory {
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.Int)
            return new NoConversion(src);
        else if(t == Type.Char)
            return new IntCharConversion(src);
        else if(t == Type.Float)
            return new IntRealConversion(src);
        else if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class RealConversionFactory extends Factory {
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.Int)
            return new RealIntConversion(src);
        else if(t == Type.Char)
            return new IntRealConversion(new CharIntConversion(src));
        else if(t == Type.Float)
            return new NoConversion(src);
        else if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class CharConversionFactory extends Factory {
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.Int)
            return new CharIntConversion(src);
        else if(t == Type.Char)
            return new NoConversion(src);
        else if(t == Type.Float)
            return new IntRealConversion(new CharIntConversion(src));
        else if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class BoolConversionFactory extends Factory {
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class StrConversionFactory extends Factory {
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.Str)
            return new NoConversion(src);
        else
            return null;
    }
}

class OtherConversionFactory extends Factory {
    public  Conversion getConversion(Expr src,Type t){
        if(t == Type.Str)
            return new StrConversion(src);
        else
            return null;
    }
}

class ConversionFactoryFactory {
    static final IntConversionFactory intf = new IntConversionFactory();
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
        else if(t == Type.Float)
            return realf;
        else if(t == Type.Str)
            return strf;
        else if(t == Type.Bool)
            return boolf;
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