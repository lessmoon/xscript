package inter;

import lexer.*;
import symbols.*;

public abstract class Expr extends Node {
    public Token op;
    public Type type;
    Expr(Token tok,Type p){
        op = tok;
        type = p;
    }

    abstract boolean isChangeable();
    public abstract Constant getValue();
    public Expr optimaze() {
        return this;
    }
    
    public String toString(){
        return op.toString();
    }
}