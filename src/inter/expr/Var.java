package inter.expr;

import lexer.Token;
import symbols.Type;

public abstract class Var extends Expr {
    public Var(Token w,Type t){
        super(w,t);
    }

    @Override
    public boolean isChangeable(){
        return true;
    }

    @Override
    public abstract Value getValue();
    
    public abstract Value setValue(Value v);

    @Override
    public Expr readOnly(){
        return new ReadOnlyWrap(this);
    }
}