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
    
    public Constant getValue(){
        Constant v = expr.getValue();
        System.out.println("set " + id + " as " + v);
        return id.setValue(v);
    }
}