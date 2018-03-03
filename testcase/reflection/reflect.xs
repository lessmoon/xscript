native<extension.reflection>{
    "Root":struct Object;
    struct StructType{
        def this();
        def Object newInstance(Object[] args);
        def bool isChildOf(StructType t);
        def bool equals(StructType t);
        def string getName();
    };

    "Root":struct Object{
        def this();
        def StructType getType();
    };
    
    struct JNClass;
    struct JNMethod;
    struct JNObject;

    struct JNClass{
        //init function
        def this(string arg);
        //functions
        def string getName();
        def JNObject newInstance(JNObject[] args);
        def JNMethod getMethod(string name,JNClass[] types);
        def bool isNull();
        def bool equals(JNClass type);
        def bool isAssignableFrom(JNClass type);
        def bool isInstance(JNObject object);
    };

    struct JNMethod{
        //init function
        def this();
        //functions
        def string getName();
        def JNClass getReturnType();
        def JNObject invoke(JNObject object,JNObject[] args);
    };

    struct JNObject{
        //init function
        def this();
        //functions
        def string  toString();
        def bool    isNull();
        def JNClass getClass();
        def bigint  castLong();
        def bool    castBool();
        def string  castString();
        def real    castReal();
        def bigreal castDouble();
        def bigint  castBigInt();
        def bigreal castBigReal();
        def int     castInt();
        def char    castChar();
         //static funtions      
        def JNObject Null();
        def JNObject newString(string str);
        def JNObject newInteger(int i);
        def JNObject newLong(int l);
        def JNObject newBoolean(bool b);
        def JNObject newDouble(real r);
        def JNObject newBigInteger(bigint bi);
        def JNObject newBigDecimal(bigreal br);
        def JNObject newCharacter(char c);
        def JNObject newFloat(real f);
    };

}
const JNObject jnObject = new JNObject;
import "../lib/system.xs";
//test code
if(_isMain_){
    JNObject[] object = { jnObject.newString("sss"),jnObject.newInteger(22),
                        jnObject.newLong(2343333),jnObject.newBoolean(true),
                        jnObject.newDouble(1.33344r),jnObject.newBigInteger(4I),
                        jnObject.newBigDecimal(22.22R),jnObject.newCharacter('c'),
                        jnObject.newFloat(4.3r)};
    auto cList = new JNClass("java.util.ArrayList");
    auto list = cList.newInstance(new JNObject[]{});
    auto mAdd = cList.getMethod("add",new JNClass[]{new JNClass("java.lang.Object")});
    auto args = new JNObject[1];
    for(int i = 0; i < sizeof object;i ++){
        args[0] = object[i];
        mAdd.invoke(list,args);
    }
    println(list.toString());
}