package inter;

import lexer.*;import symbols.*;

public class Set extends Expr {
    public Var id;
    public Expr expr;
    public Set(Token tok,Expr i,Expr x){
        super(tok,null);
        if(!(i instanceof Var)){
            error("Operand `" + op + "' should be used between variable and expression");
        }

        id = (Var)i;
        expr = x;
        type =  check();
        if( type == null ){
            error("Operand `" + op + "' should be used between " + id.type  + " variable and expression,actually " + id.type + "=" + expr.type);
        }
        if(expr.type != type){
            expr = ConversionFactory.getConversion(expr,type);
        }
    }
    
    Type check(){
        if( op.tag == Tag.MODASS ){
            if(Type.max(id.type,expr.type) == Type.Int){
                return id.type;
            } else if( Type.max(id.type,expr.type) == Type.Char ) {
                return Type.Char;
            } else
                return null;
        } else if( id.type == Type.Str ){
            if(expr.type == Type.Str)
                return Type.Str;
            else if(op.tag != Tag.ADDASS)
                return null;
        }
        return id.type;
    }

    boolean isChangeable(){
        return true;
    }

    public Expr optimize(){
        expr = expr.optimize();
        return this;
    }

    public Constant getValue(){
        Constant v = expr.getValue();
        return id.setValue(v);
    }
}