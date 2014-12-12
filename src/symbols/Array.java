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
    
    public boolean equals( Type t ){
        if(this == t)
            return true;
        if(!(t instanceof Array))
            return false;
        /* 1-dimension array just match types*/
        if(!(of instanceof Array) && !(((Array)t).of instanceof Array)){
            return of.equals(((Array)t).of);
        }
        return size == ((Array)t).size && of.equals(((Array)t).of) ;
    }

    public int getElementNumber(){
        return size;
    }
    
    public String toString(){
        return "[" + size + "] " + of.toString();
    }

}