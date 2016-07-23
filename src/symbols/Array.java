package symbols;

import inter.expr.Constant;
import lexer.Tag;

public class Array extends Type {
    public final Type of;

    public Array(Type t){
        super("[]",Tag.BASIC,Constant.Null);
        of = t;
    }

    @Override
    public boolean isBuiltInType(){
        return false;
    }

    @Override
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
        }

        return t2.of == t1.of;
    }

    @Override
    public String toString(){
        return  of.toString() + "[]" ;
    }
}