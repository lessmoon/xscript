package inter.expr;

import lexer.Token;
import inter.util.Node;
import symbols.Type;

public abstract class Expr extends Node {
    public Token op;
    public Type type;
    static public final Expr VoidExpr = new Expr(Type.Void,Type.Void){
        @Override
        boolean isChangeable(){
            return true;
        }

        @Override
        public Constant getValue(){
            return Constant.Null;
        }

        @Override
        public String toString(){
            return "void expr";
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
    
    @Override
    public String toString(){
        return getClass().getSimpleName();
    }
}