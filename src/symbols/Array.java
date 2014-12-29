package symbols;

import lexer.*;

public class Array extends Type {
    public final Type of;
    public final int  size;

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
        /*we don't care the first dimension*/
        Array t1 = this;
        Array t2 = (Array)t;

        for(;t1.of instanceof Array;t1 = (Array)t1.of,t2 = (Array)t2.of){
            if(!(t2.of instanceof Array)){
                return false;
            }
            //if(t1.size != t2.size)
                //return false;
        }
        
        return t2.of == t1.of;
    }

    public int getElementNumber(){
        return size;
    }
    
    public String toString(){
        return  of.toString() + "[]" ;
    }
}