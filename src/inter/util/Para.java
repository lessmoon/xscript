package inter;

import symbols.*;
import lexer.*;

public class Para {
    public final Type  type;
    public final Token name;
    public Para(Type t,Token n){
        type = t;
        name = n;
    }
    public String toString(){
        return type.toString() + " " + name.toString();
    }
}