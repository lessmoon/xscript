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
            error("Operand `" + op + "' should be used between " + id.type  + " variable and expression");
        }
        if(expr.type != type){
            expr = ConversionFactory.getConversion(expr,type);
        }
    }
    
    Type check(){
        if( op.tag == Tag.MODASS ){
            if( id.type != Type.Int || expr.type != Type.Int ){
                return null;
            }
            return Type.Int;
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
        switch(op.tag){
        case '=':
        case Tag.ADDASS:
        case Tag.MINASS:
        case Tag.MULTASS:
        case Tag.DIVASS:
        case Tag.MODASS:
        }
        
        return id.setValue(v);
    }
}