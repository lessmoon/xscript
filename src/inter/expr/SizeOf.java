package inter.expr;

import lexer.Token;
import symbols.Array;
import symbols.Type;

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

    @Override
    boolean isChangeable(){
        return expr.isChangeable();
    }

    @Override
    public Expr optimize(){
        expr = expr.optimize();
        if(isChangeable()){
            return this;
        } else {
            return getValue();
        }
    }

    @Override
    public Constant getValue(){
        Constant c = expr.getValue();
        if(c == Constant.Null){
            error("null pointer error:try to get a null array size");
        }
        ArrayConst ac = (ArrayConst)c;
        return new Constant(ac.getSize());
    }

    @Override
    public String toString(){
        return op.toString() + " " + expr.toString();
    }
}