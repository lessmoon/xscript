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
    
    public int getSize() {
        return size * of.getSize();
    }
    
    public int getElementNumber(){
        return size;
    }
    
    public String toString(){
        return "[" + size + "] " + of.toString();
    }
}