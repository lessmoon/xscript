package inter.util;

import lexer.Token;
import symbols.Type;

public class Param {
    public final Type  type;
    public final Token name;
    public Param(Type t, Token n){
        type = t;
        name = n;
    }
    public String toString(){
        return type.toString() + " " + name.toString();
    }
}