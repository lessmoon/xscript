package symbols;

import inter.expr.Value;
import lexer.Tag;

public class Array extends Type {
    public final Type of;

    public Array(Type t){
        super("[]",Tag.BASIC, Value.Null);
        of = t;
    }

    @Override
    public boolean isBuiltInType(){
        return false;
    }

    @Override
    public boolean isCongruentWith(Type type){
        if(this == type)
            return true;
        if(!(type instanceof Array))
            return false;
        /*we don't care the first dimension*/
        Array t1 = this;
        Array t2 = (Array) type;

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