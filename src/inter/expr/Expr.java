package inter.expr;

import lexer.*;
import symbols.*;
import inter.util.Node;

public abstract class Expr extends Node {
    public Token op;
    public Type type;
    static public final Expr VoidExpr = new Expr(Type.Void,Type.Void){
        boolean isChangeable(){
            return true;
        }
        public Constant getValue(){
            return Constant.False;
        }
    };
    
    Expr(Token tok,Type p){
        op = tok;
        type = p;
    }

    abstract boolean isChangeable();
    public abstract Constant getValue();
    
    public Expr optimize() {
        return this;
    }
    
    public String toString(){
        return getClass().getName();
    }
}