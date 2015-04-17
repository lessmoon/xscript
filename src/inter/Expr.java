package inter;

import lexer.*;
import symbols.*;

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
    public abstract Expr clone();
    
    public Expr reduce(){
        return this;
    }

    public Expr optimize() {
        return this;
    }
    
    public String toString(){
        return getClass().getName();
    }

}