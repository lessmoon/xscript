package inter;

import lexer.*;
import symbols.*;

class IntRel extends Rel {
    public IntRel(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }
    
    public Constant getValue(){
        assert(expr1!=null);
        assert(expr2!=null);
        assert(expr1.getValue()!=null);
        assert(expr2.getValue()!=null);
        assert(expr1.getValue().op != null);
        assert(expr2.getValue().op != null);
        int l = ((Num)(expr1.getValue().op)).value;
        int r = ((Num)(expr2.getValue().op)).value;

        switch(op.tag){
        case '>':
            return  l > r?Constant.True :Constant.False;
        case '<':
            return  l < r?Constant.True :Constant.False;
        case Tag.EQ:
            return  l == r?Constant.True :Constant.False;
        case Tag.NE:
            return  l != r?Constant.True :Constant.False;
        case Tag.GE:
            return  l >= r?Constant.True :Constant.False;
        case Tag.LE:
            return  l <= r?Constant.True :Constant.False;
        default:
            /*error*/
            return null;
        }
    }
}

class CharRel extends Rel {
    public CharRel(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }
    
    public Constant getValue(){
        assert(expr1!=null);
        assert(expr2!=null);
        assert(expr1.getValue()!=null);
        assert(expr2.getValue()!=null);
        assert(expr1.getValue().op != null);
        assert(expr2.getValue().op != null);
        char l = ((Char)(expr1.getValue().op)).value;
        char r = ((Char)(expr2.getValue().op)).value;

        switch(op.tag){
        case '>':
            return  l > r?Constant.True :Constant.False;
        case '<':
            return  l < r?Constant.True :Constant.False;
        case Tag.EQ:
            return  l == r?Constant.True :Constant.False;
        case Tag.NE:
            return  l != r?Constant.True :Constant.False;
        case Tag.GE:
            return  l >= r?Constant.True :Constant.False;
        case Tag.LE:
            return  l <= r?Constant.True :Constant.False;
        default:
            /*error*/
            return null;
        }
    }
}

class BoolRel extends Rel {
    public BoolRel(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
        if(tok.tag != Tag.EQ)
            error("Operand "+ tok + " forbidden:" + x1.type);
    }
    
    public Constant getValue(){
        return expr1.getValue() == expr2.getValue()? Constant.True :Constant.False;
    }

}

class RealRel extends Rel {
    public RealRel(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }
    
    public Constant getValue(){
        float l = ((Real)(expr1.getValue().op)).value;
        float r = ((Real)(expr2.getValue().op)).value;

        switch(op.tag){
        case '>':
            return  l > r?Constant.True :Constant.False;
        case '<':
            return  l < r?Constant.True :Constant.False;
        case Tag.EQ:
            return  l == r?Constant.True :Constant.False;
        case Tag.NE:
            return  l != r?Constant.True :Constant.False;
        case Tag.GE:
            return  l >= r?Constant.True :Constant.False;
        case Tag.LE:
            return  l <= r?Constant.True :Constant.False;
        default:
            /*error*/
            return null;
        }
    }
}

class StrRel extends Rel {
    public StrRel(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }
    
    public Constant getValue(){
        String l = ((Str)(expr1.getValue().op)).value;
        String r = ((Str)(expr2.getValue().op)).value;
        int delt = l.compareTo(r);
        switch(op.tag){
        case '>':
            return  delt > 0?Constant.True :Constant.False;
        case '<':
            return  delt < 0?Constant.True :Constant.False;
        case Tag.EQ:
            return  delt == 0?Constant.True :Constant.False;
        case Tag.NE:
            return  delt != 0?Constant.True :Constant.False;
        case Tag.GE:
            return  delt >= 0?Constant.True :Constant.False;
        case Tag.LE:
            return  delt <= 0?Constant.True :Constant.False;
        default:
            /*error*/
            return null;
        }

    }
}

public class RelFactory {
    public static Rel getRel(Token tok,Expr x1,Expr x2){
        if( x1.type == Type.Str ){
            if( x2.type != Type.Str ){
                /*error*/
                return null;
            }
            return new StrRel(tok,x1,x2);
        } else {
            Type t = Type.max(x1.type,x2.type);
            if(t == null)
                return null;
            if( t != x1.type )
                x1 = ConversionFactory.getConversion(x1,t);
            if( t != x2.type )
                x2 = ConversionFactory.getConversion(x2,t);
            if( t == Type.Int )
                return new IntRel(tok,x1,x2);
            else if(t == Type.Bool)
                return new BoolRel(tok,x1,x2);
            else if(t == Type.Char)
                return new CharRel(tok,x1,x2);
            else if(t == Type.Float)
                return new RealRel(tok,x1,x2);
            else
                return null;
        }

    }
}