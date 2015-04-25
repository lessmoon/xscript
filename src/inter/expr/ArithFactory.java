package inter.expr;

import lexer.*;
import symbols.*;

import java.math.BigInteger;
import java.math.BigDecimal;

class IntArith extends Arith {
    public IntArith(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }

    public boolean check(){
        switch(op.tag){
        case '+':
        case '-':
        case '*':
        case '/':
        case '%':
            return true;
        default:/*error*/
            return false;
        }
    }

    @Override
    public Constant getValue(){
        int lv = ((Num)(expr1.getValue().op)).value;
        int rv = ((Num)(expr2.getValue().op)).value;
        Constant v = null;
        switch(op.tag){
        case '+':
            return new Constant(lv + rv);
        case '-':
            return new Constant(lv - rv);
        case '*':
            return new Constant(lv * rv);
        case '/':
            return new Constant(lv / rv);
        case '%':
            return new Constant(lv % rv);
        default:/*error*/
            return null;
        }

    }

}

class BigIntArith extends Arith{
    public BigIntArith(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }

    public boolean check(){
        switch(op.tag){
        case '+':
        case '-':
        case '*':
        case '/':
        case '%':
            return true;
        default:/*error*/
            return false;
        }
    }
    
    @Override
    public Constant getValue(){
        BigInteger lv = ((BigNum)(expr1.getValue().op)).value;
        BigInteger rv = ((BigNum)(expr2.getValue().op)).value;
        Constant v = null;
        switch(op.tag){
        case '+':
            return new Constant(lv.add(rv));
        case '-':
            return new Constant(lv.subtract(rv));
        case '*':
            return new Constant(lv.multiply(rv));
        case '/':
            return new Constant(lv.divide(rv));
        case '%':
            return new Constant(lv.mod(rv));
        default:/*error*/
            return null;
        }
    }
}

class RealArith extends Arith {
    public RealArith(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }

    public boolean check(){
        switch(op.tag){
        case '+':
        case '-':
        case '*':
        case '/':
            return true;
        default:/*error*/
            return false;
        }
    }
    
    public Constant getValue(){
        float lv = ((lexer.Float)(expr1.getValue().op)).value;
        float rv = ((lexer.Float)(expr2.getValue().op)).value;

        Constant v = null;
        switch(op.tag){
        case '+':
            return new Constant(lv + rv);
        case '-':
            return new Constant(lv - rv);
        case '*':
            return new Constant(lv * rv);
        case '/':
            return new Constant(lv / rv);
        default:/*error*/
            return null;
        }
    }
}

class BigRealArith extends Arith{
    public BigRealArith(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }

    public boolean check(){
        switch(op.tag){
        case '+':
        case '-':
        case '*':
        case '/':
            return true;
        default:/*error*/
            return false;
        }
    }
    
    public Constant getValue(){
        BigDecimal lv = ((BigFloat)(expr1.getValue().op)).value;
        BigDecimal rv = ((BigFloat)(expr2.getValue().op)).value;
        Constant v = null;
        switch(op.tag){
        case '+':
            return new Constant(lv.add(rv));
        case '-':
            return new Constant(lv.subtract(rv));
        case '*':
            return new Constant(lv.multiply(rv));
        case '/':
            return new Constant(lv.divide(rv));
        default:/*error*/
            return null;
        }
    }
}

class CharArith extends Arith {  
    public CharArith(Token op,Expr x1,Expr x2){
        super(op,x1,x2);
    }

    public boolean check(){
        switch(op.tag){
        case '+':
        case '-':
        case '*':
        case '/':
        case '%':
            return true;
        default:/*error*/
            return false;
        }
    }

    public Constant getValue(){
        char lv = ((Char)(expr1.getValue().op)).value;
        char rv = ((Char)(expr2.getValue().op)).value;

        Constant v = null;
        switch(op.tag){
        case '+':
            return new Constant((char)(lv + rv));
        case '-':
            return new Constant((char)(lv - rv));
        case '*':
            return new Constant((char)(lv * rv));
        case '/':
            return new Constant((char)(lv / rv));
        case '%':
            return new Constant((char)(lv % rv));
        default:/*error*/
            return null;
        }
    }
}

class StringCat extends Arith {  
    public StringCat(Token op,Expr x1,Expr x2){
        super(op,x1,x2);
        if(expr1.type != Type.Str || expr2.type != Type.Str)
            error("String can't cat");
    }
    public boolean check(){
        switch(op.tag){
        case '+':
            return true;
        default:/*error*/
            return false;
        }
    }

    public Constant getValue(){
        Constant str1 = expr1.getValue();
        Constant str2 = expr2.getValue();
        String str = ((Str)(str1.op)).value + ((Str)(str2.op)).value;
        return new Constant(str);
    }
}

public class ArithFactory {
    public static Arith getArith(Token tok,Expr e1,Expr e2){
        Type t = Type.max(e1.type,e2.type);

        if(Type.numeric(t) || t == Type.Str){
            if(e1.type != t){
                e1 = ConversionFactory.getConversion(e1,t);
            }
            if(e2.type != t){
                e2 = ConversionFactory.getConversion(e2,t);
            }
            if(t == Type.Int){
                return new IntArith(tok,e1,e2);
            } else if(t == Type.Real){
                return new RealArith(tok,e1,e2);
            } else if(t == Type.Char){
                return new CharArith(tok,e1,e2);
            } else if(t == Type.Str){
                if(tok.tag == '+'){
                    return new StringCat(tok,e1,e2);
                }
            } else if(t == Type.BigInt){
                return new BigIntArith(tok,e1,e2);
            } else if(t == Type.BigReal){
                return new BigRealArith(tok,e1,e2);
            }
        }
        e1.error("Operand `" + tok + "' can't be used between `" + e1.type + "' and `" + e2.type + "'");
        return null;
    }
}