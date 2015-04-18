package inter.expr;

import lexer.*;
import symbols.*;

public class SizeOf extends Op{
    public Expr expr;
    public SizeOf(Token tok,Expr x){
        super(tok,Type.Int);
        expr = x;
        check();
    }

    void check(){
        if(! (expr.type instanceof Array)){
            error("Operand `" + op + "' can't be used for `" + expr.type + "',array type wanted" );
        }
    }
    
    boolean isChangeable(){
        return expr.isChangeable();
    }

    public Expr optimize(){
        if(isChangeable()){
            expr = expr.optimize();
            return this;
        } else {
            return getValue();
        }
    }

    public Constant getValue(){
        ArrayConst c = (ArrayConst)expr.getValue();
        return new Constant(c.size);
    }

    public String toString(){
        return op.toString() + " " + expr.toString();
    }
}