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
        /*we don't care the first dimension*/
        Type t1 = of;
        Type t2 = ((Array)t).of;
        for(;t1 instanceof Array;t1 = ((Array)t1).of,t2 = ((Array)t2).of){
            if(!(t2 instanceof Array) || t2.getElementNumber() != t1.getElementNumber())
                return false;
        }
        return t2 == t1;
    }

    public int getElementNumber(){
        return size;
    }
    
    public String toString(){
        return  of.toString() + "[" + size + "]" ;
    }

}