package symbols;

import lexer.*;

public class Type extends Word {

    public Type(String s,int tag){
        super(s,tag);
    }

    public int getSize(){
        return 1;
    }
    
    public static final Type 
        Int     =   new Type( "int" , Tag.BASIC),
        Float   =   new Type( "real" , Tag.BASIC),
        Str     =   new Type( "string" , Tag.BASIC),
        Char    =   new Type( "char" , Tag.BASIC),
        Bool    =   new Type( "bool" , Tag.BASIC);

    public static boolean numeric(Type p){
        return (p == Type.Int || p == Type.Float || p == Type.Char);
    }
    
    public static Type max(Type p1,Type p2){
        if( p1 == Type.Str )
            return Type.Str;
        else if( !numeric(p1) || !numeric(p2))
            return null;
        else if(p1 == Type.Float || p2 == Type.Float)
            return Type.Float;
        else if(p1 == Type.Int || p2 == Type.Int)
            return Type.Int;
        else
            return Type.Char;
    }
}