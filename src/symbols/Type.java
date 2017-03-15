package symbols;

import inter.expr.Value;
import lexer.Tag;
import lexer.Word;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Type extends Word {
    private Value initialValue ;

    public Type(String s,int tag){
        super(s,tag);
        initialValue = null;
    }

    protected Type(String s,int tag,Value val){
        super(s,tag);
        initialValue = val;
    }

    /**
     * Two type are congruent with each other iff:
     *     1.they are totally same or
     *     2.they are array types and they have congruent component types
     * @param type the type to compare with
     * @return if {@param type} is congruent with this type
     */
    public boolean isCongruentWith(Type type ){
        return this == type;
    }

    /**
     *  A type is not built-in iff it can't be used as reference
     *  There are two types which are built-in:{@link Struct},{@link #Null} and {@link Array}
     *  @return if the type is built-in
     */
    public boolean isBuiltInType(){
        return true;
    }

    /**
     *  Every type needs an initial value in case that undefined behaviors.
     *  That is "declaration is initialization"
     * @return the initial value of a type
     */
    public Value getInitialValue(){
        return initialValue;
    }
    
    private void setInitialValue(Value c){
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

    static {
        Int.setInitialValue(new Value(0));
        Real.setInitialValue(new Value(0.0f));
        Str.setInitialValue(new Value(""));
        Char.setInitialValue(new Value('\0'));
        Bool.setInitialValue(Value.False);
        BigInt.setInitialValue(new Value(BigInteger.ZERO));
        BigReal.setInitialValue(new Value(BigDecimal.ZERO));
    }
        
    public static boolean numeric(Type p){
        return (p == Type.Int || p == Type.Real || p == Type.Char
                || p == Type.BigInt || p == Type.BigReal);
    }


    /**
     * If T1 and T2 are both numerical,bool or string ,the max type of T1 and T2 is
     *    1.T1 if T1 == T2
     *    2.In two partial ordering relations(= for the same types)
     *                  / real
     *         bigreal >        > int > char
     *                  \ bigint
     *       the minimum type can >= T1 and T2 in both relations.
     *       e.g: max(string,string) = string,
     *            max(int,real) = real
     *            max(bigint,real) = bigreal
     * @param type1 the left type
     * @param type2 the right type
     * @return the max type or null if either {@param type1} or {@param type2} is not numeric,bool or string
     */
    public static Type max(Type type1,Type type2){
        if( type1 == Type.Str ){
            return Type.Str;
        } else if(type1 == Type.Bool && type2 ==Type.Bool) {
            return Type.Bool;
        } else if( !numeric(type1) || !numeric(type2)){
            return null;
        } else if(type1 == Type.BigReal || type2 == Type.BigReal){
            return Type.BigReal;
        } else if( type1 == Type.Real ){
            return type2 == Type.BigInt?Type.BigReal:Type.Real;
        } else if( type2 == Type.Real ){
            return type1 == Type.BigInt?Type.BigReal:Type.Real;
        } else if(type1 == Type.BigInt || type2 == Type.BigInt){
            return Type.BigInt;
        } else if(type1 == Type.Int || type2 == Type.Int){
            return Type.Int;
        } else {
            return Type.Char;
        }
    }
}