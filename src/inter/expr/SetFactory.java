package inter.expr;

import lexer.*;
import symbols.*;

import java.math.BigInteger;
import java.math.BigDecimal;

class BigIntSet extends Set {
    public BigIntSet(Token tok,Expr i,Expr x){
        super(tok,i,x);
    }
    
    public Constant getValue(){
        BigInteger r = ((BigNum)(expr.getValue().op)).value;
        BigInteger l = ((BigNum)(id.getValue().op)).value;
        switch(op.tag){
        case Tag.ADDASS:
            return id.setValue( new Constant(l.add(r)));
        case Tag.MINASS:
            return id.setValue( new Constant(l.subtract(r)));
        case Tag.MULTASS:
            return id.setValue( new Constant(l.multiply(r)));
        case Tag.DIVASS:
            return id.setValue( new Constant(l.divide(r)));
        case Tag.MODASS:
            return id.setValue( new Constant(l.mod(r)));
        default:
            return null;
        }
    }
}

class IntSet extends Set {
    
    public IntSet(Token tok,Expr i,Expr x){
        super(tok,i,x);
    }

    public Constant getValue(){
        int r = ((Num)(expr.getValue().op)).value;
        int l = ((Num)(id.getValue().op)).value;

        switch(op.tag){
        case Tag.ADDASS:
            return id.setValue( new Constant(l + r));
        case Tag.MINASS:
            return id.setValue( new Constant(l - r));
        case Tag.MULTASS:
            return id.setValue( new Constant(l * r));
        case Tag.DIVASS:
            return id.setValue( new Constant(l / r));
        case Tag.MODASS:
            return id.setValue( new Constant(l % r));
        default:
            return null;
        }
    }
}

class CharSet extends Set {
    
    public CharSet(Token tok,Expr i,Expr x){
        super(tok,i,x);
    }

    public Constant getValue(){
        char r = ((Char)(expr.getValue().op)).value;
        char l = ((Char)(id.getValue().op)).value;
        
        switch(op.tag){
        case Tag.ADDASS:
            return id.setValue( new Constant(l + r));
        case Tag.MINASS:
            return id.setValue( new Constant(l - r));
        case Tag.MULTASS:
            return id.setValue( new Constant(l * r));
        case Tag.DIVASS:
            return id.setValue( new Constant(l / r));
        case Tag.MODASS:
            return id.setValue( new Constant(l % r));
        default:
            return null;
        }
    }
}

class BigRealSet extends Set {
    public BigRealSet(Token tok,Expr i,Expr x){
        super(tok,i,x);
    }
    
    public Constant getValue(){
        BigDecimal r = ((BigFloat)(expr.getValue().op)).value;
        BigDecimal l = ((BigFloat)(id.getValue().op)).value;
        switch(op.tag){
        case Tag.ADDASS:
            return id.setValue( new Constant(l.add(r)));
        case Tag.MINASS:
            return id.setValue( new Constant(l.subtract(r)));
        case Tag.MULTASS:
            return id.setValue( new Constant(l.multiply(r)));
        case Tag.DIVASS:
            return id.setValue( new Constant(l.divide(r)));
        default:
            return null;
        }
    }
}

class RealSet extends Set {
    public RealSet(Token tok,Expr i,Expr x){
        super(tok,i,x);
    }
    
    public Constant getValue(){
        float r = ((lexer.Float)(expr.getValue().op)).value;
        float l = ((lexer.Float)(id.getValue().op)).value;

        switch(op.tag){
        case Tag.ADDASS:
            return id.setValue( new Constant(l + r));
        case Tag.MINASS:
            return id.setValue( new Constant(l - r));
        case Tag.MULTASS:
            return id.setValue( new Constant(l * r));
        case Tag.DIVASS:
            return id.setValue( new Constant(l / r));
        default:
            return null;
        }
    }
}

class StrSet extends Set {
    public StrSet(Token tok,Expr i,Expr x){
        super(tok,i,x);
    }

    public Constant getValue(){
        String r = ((Str)(expr.getValue().op)).value;
        String l = ((Str)(id.getValue().op)).value;
        
        switch(op.tag){
        case Tag.ADDASS:
            return id.setValue( new Constant(l + r));
        default:
            return null;
        }
    }
}

public class SetFactory {
    static public Set getSet(Token tok,Expr i,Expr x){
        if(tok.tag == '='){
            return new Set(tok,i,x);
        } else if( i.type == Type.Int ){
            return new IntSet(tok,i,x);
        } else if( i.type == Type.Real){
            return new RealSet(tok,i,x);
        } else if( i.type == Type.Str){
            return new StrSet(tok,i,x);
        } else if( i.type == Type.Char){
            return new CharSet(tok,i,x);
        } else if( i.type == Type.BigInt){
            return new BigIntSet(tok,i,x);
        } else/*( i.type == Type.BigReal)*/{
            return new BigRealSet(tok,i,x);
        }
    }
}