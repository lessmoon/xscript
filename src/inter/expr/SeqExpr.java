package inter.expr;

import lexer.*;
import symbols.*;

/*
 * calculate a first
 * (a,b) return a
 */
public class SeqExpr extends Expr {
    Expr left;
    Expr right;

    public SeqExpr(Token tok,Expr left,Expr right){
        super(tok,left.type);
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString(){
        return "(" + left + "," + right + ")";
    }

    @Override
    public Expr optimize(){
        left = left.optimize();
        right = right.optimize();
        if(right.isChangeable())
            return this;
        else 
            return left;
    }

    @Override
    boolean isChangeable(){
        return left.isChangeable() || right.isChangeable();
    }

    @Override
    public Constant getValue(){
        Constant c = left.getValue();
        right.getValue();
        return c;
    }
}

