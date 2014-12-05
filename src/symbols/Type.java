package symbols;

import lexer.*;

public class Type extends Word {

    public Type(String s,int tag){
        super(s,tag);
    }

    public static final Type 
        Int     =   new Type( "int" , Tag.BASIC),
        Float   =   new Type( "real" , Tag.BASIC),
        Str     =   new Type( "string" , Tag.BASIC),
        Bool    =   new Type( "bool" , Tag.BASIC);

    public static boolean numeric(Type p){
        if(p == Type.Int || p == Type.Float ) 
            return true;
        else 
            return false;
    }
    
    public static Type max(Type p1,Type p2){
        if( p1 == Type.Str )
            return Type.Str;
        else if( !numeric(p1) || !numeric(p2))
            return null;
        else if(p1 == Type.Float || p2 == Type.Float)
            return Type.Float;
        else
            return Type.Int;
    }
}