package symbols;

import lexer.*;

public class Array extends Type {
    public Type of;
    public int  size;
    
    public Array(Type t,int sz){
        super("[" + sz + "]",Tag.ARRAY);
        of = t;
        size =  sz;
    }

    public String toString(){
        return "[" + size + "] " + of.toString();
    }
}