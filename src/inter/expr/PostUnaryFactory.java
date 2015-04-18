package inter.expr;

import lexer.*;
import symbols.*;

class IntPostUnary extends Unary {
    public IntPostUnary(Token tok,Expr x){
        super(tok,x);
        check();
    }

    void check(){
        switch(op.tag){
        case Tag.INC:
        case Tag.DEC:
            if(!(expr instanceof Var))
                error("operand `" + op + "' should be used for variable");
            return;
        default:
            error("Unknown operand:`" + op + "'");
            return;
        }
    }

    public Constant getValue(){
        Constant c = expr.getValue();
        switch(op.tag){
        case Tag.INC:
            ((Var)expr).setValue(new Constant(((Num)(c.op)).value + 1));
            return c;
        case Tag.DEC:
            ((Var)expr).setValue(new Constant(((Num)(c.op)).value - 1));
            return c;
        default:
            error("Unknown operand:`" + op + "'");
            return null;
        }
    }
}

class CharPostUnary extends Unary {
    public CharPostUnary(Token tok,Expr x){
        super(tok,x);
        check();
    }

    void check(){
        switch(op.tag){
        case Tag.INC:
        case Tag.DEC:
            if(!(expr instanceof Var))
                error("operand `" + op + "' should be used for variable");
            return;
        default:
            error("Unknown operand:`" + op + "'");
            return;
        }
    }

    public Constant getValue(){
        Constant c = expr.getValue();
        switch(op.tag){
        case Tag.INC:
            ((Var)expr).setValue(new Constant((char)(((Char)(c.op)).value + 1)));
            return c;
        case Tag.DEC:
            ((Var)expr).setValue(new Constant((char)(((Char)(c.op)).value - 1)));
            return c;
        default:
            error("Unknown operand:`" + op + "'");
            return null;
        }
    }
}

public class PostUnaryFactory {
    public static Unary getUnary(Token tok,Expr e){
        if( Type.numeric(e.type) ){
            if( Type.Int == e.type ){
                return new IntPostUnary(tok,e);
            } else if( Type.Char == e.type ){
                return new CharPostUnary(tok,e);
            } else {
                e.error("Operand:`" + tok + "' is not permitted here");
            }
        }
        return null;
    }
}