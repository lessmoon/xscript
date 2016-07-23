package inter.expr;

import lexer.BigFloat;
import lexer.BigNum;
import lexer.Char;
import lexer.Num;
import lexer.Str;
import lexer.Tag;
import lexer.Token;
import inter.stmt.FunctionBasic;
import symbols.Array;
import symbols.Struct;
import symbols.Type;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.ArrayList;

class IntRel extends Rel {
    IntRel(Token tok, Expr x1, Expr x2){
        super(tok,x1,x2);
    }
    
    public Constant getValue(){
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

class BigIntRel extends Rel {
    BigIntRel(Token tok, Expr x1, Expr x2){
        super(tok,x1,x2);
    }
    
    public Constant getValue(){
        BigInteger l = ((BigNum)(expr1.getValue().op)).value;
        BigInteger r = ((BigNum)(expr2.getValue().op)).value;
        int d = l.compareTo(r);
        switch(op.tag){
        case '>':
            return  d > 0?Constant.True :Constant.False;
        case '<':
            return  d < 0?Constant.True :Constant.False;
        case Tag.EQ:
            return  d == 0?Constant.True :Constant.False;
        case Tag.NE:
            return  d != 0?Constant.True :Constant.False;
        case Tag.GE:
            return  d >= 0?Constant.True :Constant.False;
        case Tag.LE:
            return  d <= 0?Constant.True :Constant.False;
        default:
            /*error*/
            return null;
        }
    }
}

class CharRel extends Rel {
    CharRel(Token tok, Expr x1, Expr x2){
        super(tok,x1,x2);
    }
    
    public Constant getValue(){
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
    BoolRel(Token tok, Expr x1, Expr x2){
        super(tok,x1,x2);
        if(tok.tag != Tag.EQ && tok.tag != Tag.NE)
            error("Operand "+ tok + " forbidden:" + x1.type);
    }
    
    public Constant getValue(){
        if(op.tag == Tag.EQ)
            return expr1.getValue() == expr2.getValue()? Constant.True :Constant.False;
        else //Tag.NE
            return expr1.getValue() != expr2.getValue()? Constant.True :Constant.False; 
    }

}

class RealRel extends Rel {
    RealRel(Token tok, Expr x1, Expr x2){
        super(tok,x1,x2);
    }
    
    public Constant getValue(){
        float l = ((lexer.Float)(expr1.getValue().op)).value;
        float r = ((lexer.Float)(expr2.getValue().op)).value;

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

class BigRealRel extends Rel {
    BigRealRel(Token tok, Expr x1, Expr x2){
        super(tok,x1,x2);
    }
    
    public Constant getValue(){
        BigDecimal l = ((BigFloat)(expr1.getValue().op)).value;
        BigDecimal r = ((BigFloat)(expr2.getValue().op)).value;
        int d = l.compareTo(r);
        switch(op.tag){
        case '>':
            return  d > 0?Constant.True :Constant.False;
        case '<':
            return  d < 0?Constant.True :Constant.False;
        case Tag.EQ:
            return  d == 0?Constant.True :Constant.False;
        case Tag.NE:
            return  d != 0?Constant.True :Constant.False;
        case Tag.GE:
            return  d >= 0?Constant.True :Constant.False;
        case Tag.LE:
            return  d <= 0?Constant.True :Constant.False;
        default:
            /*error*/
            return null;
        }
    }
}

class StrRel extends Rel {
    StrRel(Token tok, Expr x1, Expr x2){
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

class ObjectRel extends Rel {
    ObjectRel(Token tok, Expr x1, Expr x2){
        super(tok,x1,x2);
        if(op.tag != Tag.EQ && op.tag != Tag.NE ){
            error("Operand `" + op + "' can't used between `" + x1.type + "' and `" + x2.type + "'");
        }
    }
    
    public Constant getValue(){
        Constant l = expr1.getValue();
        Constant r = expr2.getValue();
        switch(op.tag){
        case Tag.EQ:
            return  (l == r)?Constant.True:Constant.False;
        case Tag.NE:
            return  (l != r)?Constant.True:Constant.False;
        default:
            /*error*/
            return null;
        }

    }
}

public class RelFactory {
    public static Expr getRel(Token tok,Expr x1,Expr x2) {
        Expr r = null;

        if (x1 == Constant.Null) {
            if (!x2.type.isBuiltInType()) {
                if (x1.type != x2.type) {
                    x1 = ConversionFactory.getConversion(x1, x2.type);
                    return new ObjectRel(tok, x1, x2);
                }
            }
        } else if (x2 == Constant.Null) {
            if (!x1.type.isBuiltInType()) {
                x2 = ConversionFactory.getConversion(x2, x1.type);
                return new ObjectRel(tok, x1, x2);
            }
        }

        if (x1.type instanceof Struct) {
            Token fname = ((Struct) (x1.type)).getOverloading(tok);
            if (fname != null) {
                if (x2.type != x1.type) {
                    x2 = ConversionFactory.getConversion(x2, x1.type);
                }

                FunctionBasic f = ((Struct) (x1.type)).getNormalFunction(fname);
                ArrayList<Expr> p = new ArrayList<>();
                if (f != null) {
                    p.add(x1);
                    p.add(x2);
                    return new FunctionInvoke(f, p);
                }
                f = ((Struct) (x1.type)).getVirtualFunction(fname);
                if (f != null) {
                    p.add(x2);
                    return new VirtualFunctionInvoke(x1, f, p);
                }
            }
        }
        if (x1.type instanceof Array || x1.type instanceof Struct) {
            if (x1.type.equals(x2.type))
                r = new ObjectRel(tok, x1, x2);
        } else if (x1.type == Type.Str) {
            if (x2.type == Type.Str) {
                r = new StrRel(tok, x1, x2);
            }
        } else {
            Type t = Type.max(x1.type,x2.type);
            if(t != null){
                if( t != x1.type )
                    x1 = ConversionFactory.getConversion(x1,t);
                if( t != x2.type )
                    x2 = ConversionFactory.getConversion(x2,t);
                if( t == Type.Int )
                    r = new IntRel(tok,x1,x2);
                else if(t == Type.Bool)
                    r = new BoolRel(tok,x1,x2);
                else if(t == Type.Char)
                    r = new CharRel(tok,x1,x2);
                else if(t == Type.Real)
                    r = new RealRel(tok,x1,x2);
                else if(t == Type.BigInt)
                    r = new BigIntRel(tok,x1,x2);
                else if(t == Type.BigReal)
                    r = new BigRealRel(tok,x1,x2);
            }
        }
        if(r == null)
            x1.error("Operand `" + tok + "' can't be used between " + x1.type + " and " + x2.type);
        return r;
    }
}