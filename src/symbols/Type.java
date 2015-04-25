package symbols;

import lexer.*;

public class Type extends Word {

    public Type(String s,int tag){
        super(s,tag)
        ;
    }

    public int getSize(){
        return 1;
    }
    
    public int getElementNumber(){
        return 1;
    }
    
    public boolean equals( Type t ){
        return this == t;
    }

    public static final Type 
        Int     =   new Type( "int" , Tag.BASIC),
        Real    =   new Type( "real" , Tag.BASIC),
        Str     =   new Type( "string" , Tag.BASIC),
        Char    =   new Type( "char" , Tag.BASIC),
        Bool    =   new Type( "bool" , Tag.BASIC),
        Void    =   new Type( "void" , Tag.BASIC),
        BigInt  =   new Type( "bigint",Tag.BASIC),
        BigReal =   new Type( "bigreal",Tag.BASIC);

    public static boolean numeric(Type p){
        return (p == Type.Int || p == Type.Real || p == Type.Char
                || p == Type.BigInt || p == Type.BigReal);
    }
    
    public static Type max(Type p1,Type p2){
        //(int,int)>int
        //(int,(big)float)>(big)float
        //(bigint,(big)float)>bigfloat
        
        if( p1 == Type.Str ){
            return Type.Str;
        } else if( !numeric(p1) || !numeric(p2)){
            return null;
        } else if(p1 == Type.BigReal || p2 == Type.BigReal){
            return Type.BigReal;
        } else if( p1 == Type.Real ){
            return p2 == Type.BigInt?Type.BigReal:Type.Real;
        } else if( p2 == Type.Real ){
            return p1 == Type.BigInt?Type.BigReal:Type.Real;
        } else if(p1 == Type.BigInt || p2 == Type.BigInt){
            return Type.BigInt;
        } else if(p1 == Type.Int || p2 == Type.Int){
            return Type.Int;
        } else {
            return Type.Char;
        }
    }
}