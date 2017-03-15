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
    public boolean isChangeable(){
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
    public Value getValue(){
        Value c = expr.getValue();
        if(c == Value.Null){
            error("null pointer error:try to get a null array size");
        }
        ArrayValue ac = (ArrayValue)c;
        return new Value(ac.getSize());
    }

    @Override
    public String toString(){
        return op.toString() + " " + expr.toString();
    }
}