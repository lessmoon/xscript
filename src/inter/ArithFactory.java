package inter;

import lexer.*;
import symbols.*;

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
        float lv = ((Real)(expr1.getValue().op)).value;
        float rv = ((Real)(expr2.getValue().op)).value;

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

    public Expr optimize(){
        if(isChangeable()){
            expr1 = expr1.optimize();
            if(expr1 instanceof Constant
                && expr1.toString().isEmpty()){
                return expr2.optimize();
            }
            expr2 = expr2.optimize();
            if(expr2 instanceof Constant
                && expr2.toString().isEmpty()){
                return expr1;
            }
            return this;
        } else {
            return getValue();
        }
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
            } else if(t == Type.Float){
                return new RealArith(tok,e1,e2);
            } else if(t == Type.Char){
                return new CharArith(tok,e1,e2);
            } else if(t == Type.Str){
                if(tok.tag == '+'){
                    return new StringCat(tok,e1,e2);
                }
            }
        }
        e1.error("Operand `" + tok + "' can't be used between `" + e1.type + "' and `" + e2.type + "'");
        return null;
    }
}