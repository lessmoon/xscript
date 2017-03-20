package inter.expr;

import inter.util.Node;
import lexer.Token;
import symbols.Type;

public abstract class Expr extends Node {
    public Token op;
    public Type type;
    static public final Expr VoidExpr = new Expr(Type.Void,Type.Void){
        @Override
        public boolean isChangeable(){
            return true;
        }

        @Override
        public Value getValue(){
            return Value.Null;
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

    public abstract boolean isChangeable();
    public abstract Value getValue();

    public Expr optimize() {
        return this;
    }
    
    @Override
    public String toString(){
        return getClass().getSimpleName();
    }
}