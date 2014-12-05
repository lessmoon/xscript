package inter;

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
        return new Constant(e.getValue().toString());
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

public class ConversionFactory {
    public static Conversion getConversion(Expr src,Type t){
        if(src.type == t){
            return new NoConversion(src);
        }else if(t == Type.Str){
            return new StrConversion(src);
        } else if(src.type == Type.Float && t == Type.Int){
            return new RealIntConversion(src);
        } else if(src.type == Type.Int && t == Type.Float){
            return new IntRealConversion(src);
        } else {
            return null;
        }
    }
}