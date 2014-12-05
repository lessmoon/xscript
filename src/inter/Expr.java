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

    public abstract Constant getValue();

    public String toString(){
        return op.toString();
    }
}