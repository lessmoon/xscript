package inter;

import runtime.*;
import lexer.*;
import symbols.*;

/*
 * e.g. inline function def int db(int x)=(x+x);
 *          x is an ExprReference
 */
public ExprReference extends Expr {
    Expr e;

    ExprReference(Token w,Type t){
        super(w,t);
    }

    @Override
    public Constant getValue(){
        return e.getValue();
    }

    @Override
    boolean isChangeable(){
        return e == null||e.isChangeable();
    }

    public void setValue(Expr e){
        this.e = e;
    }

    @Override
    public Expr reduce(){
        return e;
    }

    /*
     * This will not be used in actual expression
     * So just return itself,for convenience and necessity
     * @See InlineTemplate
     */
    @Override
    public Expr clone(){
        return this;
    }

    @Override
    public String toString(){
        return "" + e;
    }
}