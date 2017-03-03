package symbols;

import inter.expr.Constant;
import lexer.Tag;
import lexer.Word;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Type extends Word {
    private Constant initialValue ;

    public Type(String s,int tag){
        super(s,tag);
        initialValue = null;
    }

    protected Type(String s,int tag,Constant val){
        super(s,tag);
        initialValue = val;
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
    
    public boolean isBuiltInType(){
        return true;
    }
    
    public Constant getInitialValue(){
        return initialValue;
    }
    
    protected void setInitialValue(Constant c){
        initialValue = c;
    }
    
    public static final Type 
        Int     =   new Type( "int" , Tag.BASIC),
        Real    =   new Type( "real" , Tag.BASIC),
        Str     =   new Type( "string" , Tag.BASIC),
        Char    =   new Type( "char" , Tag.BASIC),
        Bool    =   new Type( "bool" , Tag.BASIC),
        Void    =   new Type( "void" , Tag.BASIC),
        BigInt  =   new Type( "bigint",Tag.BASIC),
        BigReal =   new Type( "bigreal",Tag.BASIC),
        Auto    =   new Type("auto",Tag.AUTO),
        Null    =   new Type( "null" , Tag.BASIC ){
            @Override
            public boolean isBuiltInType(){
                return false;
            }
        };
    public static final StructPlaceHolder  = new Struct(new Token("#StructPlaceHolder#"));


    static {
        Int.setInitialValue(new Constant(0));
        Real.setInitialValue(new Constant(0.0f));
        Str.setInitialValue(new Constant(""));
        Char.setInitialValue(new Constant('\0'));
        Bool.setInitialValue(Constant.False);
        BigInt.setInitialValue(new Constant(BigInteger.ZERO));
        BigReal.setInitialValue(new Constant(BigDecimal.ZERO));
    }
        
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
        } else if(p1 == Type.Bool && p2 ==Type.Bool) {
            return Type.Bool;
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