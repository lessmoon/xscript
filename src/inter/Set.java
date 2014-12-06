package inter;

import lexer.*;import symbols.*;

public class Set extends Expr {
    public Var id;
    public Expr expr;
    public Set(Token tok,Var i,Expr x){
        super(tok,i.type);
        id = i;
        expr = x;
    }
    
    boolean isChangeable(){
        return true;
    }
    
    public Constant getValue(){
        Constant v = expr.getValue();
        return id.setValue(v);
    }
}