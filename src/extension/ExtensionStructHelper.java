package extension;

import extension.annotation.Init;
import extension.annotation.PassThisReference;
import extension.annotation.StructMethod;
import inter.expr.ArrayValue;
import inter.expr.StackVar;
import inter.expr.StructValue;
import inter.expr.Value;
import inter.stmt.FunctionBasic;
import inter.stmt.InitialFunction;
import inter.stmt.MemberFunction;
import inter.stmt.Stmt;
import inter.util.Para;
import lexer.Token;
import lexer.Word;
import runtime.Dictionary;
import runtime.LoadStruct;
import runtime.TypeTable;
import symbols.Array;
import symbols.Type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lessmoon on 2016/8/18.
 */
public class ExtensionStructHelper {

    /**
     * Generated function proxy invocation without return
     * For with-return version use {@linkplain #functionBodyWithReturn(StructValue, int, boolean, StackVar[], Type, Method, boolean, symbols.Struct)}
     *
     * @param arg0                  this reference
     * @param argLength             the args number of methods in java
     * @param needPassThisReference if we should pass this to the method
     * @param vars                  the stack vars(vm stack frame)
     * @param method                the methods
     */
    private static void functionBodyNoReturn(final StructValue arg0, final int argLength, final boolean needPassThisReference,
                                             final StackVar[] vars, final Method method) {
        final Object[] args = new Value[argLength];
        int i = 0;
        if (needPassThisReference) {
            args[0] = arg0;
            i++;
        }

        for (int j = 0; i < argLength; i++, j++) {
            args[i] = vars[j].getValue();
        }
        try {
            method.invoke(arg0.getExtension(), args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * Generate function invocation proxy  body of java method(with return)
     * For no-return version use {@linkplain #functionBodyNoReturn(StructValue, int, boolean, StackVar[], Method)}
     *
     * @param arg0                  this reference
     * @param argLength             the args number of methods in java
     * @param needPassThisReference if we should pass this to the method
     * @param vars                  the stack vars(vm stack frame)
     * @param returnType            the return type(may have some dynamic cast work to do)
     * @param method                the bound method
     * @param retFlag               if we should dynamic cast the return type
     * @param s                     the Struct type
     */
    private static void functionBodyWithReturn(final StructValue arg0, final int argLength, final boolean needPassThisReference,
                                               final StackVar[] vars, final Type returnType, final Method method, final boolean retFlag,
                                               final symbols.Struct s) {
        StructValue s1 = (StructValue) arg0.getValue();
        final Object[] args = new Value[argLength];
        int i = 0;
        if (needPassThisReference) {
            args[0] = s1;
            i++;
        }
        for (int j = 0; i < argLength; i++, j++) {
            args[i] = vars[j].getValue();
        }
        try {
            Value ret = (Value) method.invoke(s1.getExtension(), args);
            if (ret == null) {
                ret = Value.Null;
            } else if (!ret.type.isCongruentWith(returnType)) {
                if (ret != Value.Null) {
                    if (returnType instanceof symbols.Struct && ret.type instanceof symbols.Struct) {
                        if (retFlag) {
                            //fix the type dynamically
                            StructValue r = new StructValue(s);
                            r.setExtension(((StructValue) ret).getExtension());
                            ret = r;
                        } else if (!((symbols.Struct) ret.type).isChildOf((symbols.Struct) returnType)) {
                            throw new RuntimeException("error return type[ " + ret.type + "] expect:" + returnType);
                        }
                    }
                } else {
                    throw new RuntimeException("error return type[ " + ret.type + "] expect:" + returnType);
                }
            }
            Stmt.ret(ret);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Build the parameters of the method and fill the stack val array
     *
     * @param <T>          the class type
     * @param vars         this procedure will fill this stack var array
     * @param s            the struct to go
     * @param m            bound method in java
     * @param args         the args type defined in the annotation
     * @param needPassThis if this method annotated to be passed this{@linkplain PassThisReference}
     * @param typeTable    the type table
     * @param dic          the token dictionary
     * @param clazz        the clazz
     * @return the param generated by this procedure
     */
    private static <T> List<Para> checkAndBuildParams(final StackVar[] vars, symbols.Struct s, Method m, String[] args, final boolean needPassThis,
                                                      TypeTable typeTable, Dictionary dic, Class<T> clazz) {
        final List<Para> param = new ArrayList<>();
        int j = needPassThis ? 1 : 0;
        param.add(new Para(s, Word.This));
        final Parameter[] ps = m.getParameters();

        for (int i = 0; i < args.length; i++, j++) {
            String typeStr = args[i];
            final Type t = getType(typeStr, s, dic, typeTable, clazz);

            assert (t != Type.Void);
            assert (t != null)
                    : ("declared arg[" + (i + 1) + "] of `" + s.getName() + "." + m.getName() + "()' type[" + typeStr + "] is not found");
            assert (Value.class.isAssignableFrom(ps[j].getType()))
                    : ("implementation of declared arg[" + (i + 1) + "] of `" + s.getName() + "." + m.getName() + "()':can't bind type[" + t + "] to [" + ps[j].getType().getName() + "] ");

            if (ArrayValue.class.isAssignableFrom(ps[j].getType())) {
                //check if it is an array arg declared
                assert (t instanceof Array)
                        : ("implementation of declared arg[" + (i + 1) + "] of `" + s.getName() + "." + m.getName() + "()':can't bind type[" + t + "] to [" + ps[j].getType().getName() + "] ");
            } else if (Struct.class.isAssignableFrom(ps[j].getType())) {
                //check if it is an array arg declared
                assert (t instanceof symbols.Struct)
                        : ("implementation of declared arg[" + (i + 1) + "] of `" + s.getName() + "." + m.getName() + "()':can't bind type[" + t + "] to [" + ps[j].getType().getName() + "] ");
            }

            Token name = dic.getOrReserve("arg#" + i);
            vars[i] = new StackVar(name, t, 0, i + 1);
            param.add(new Para(t, name));
        }
        return param;
    }

    /**
     * Get type by {@param typeStr}
     * The type is either Basic type(int,real,string,bool,void,bigreal and bigint) or Struct type.
     * NOTE:Array type is not supported yet.
     * Some special case:
     *     1.Type strings starting with "#" means find struct bound by the class named by the substring after #
     *     2.Type string starting with "#." means relatively searching the class(based on package of the class)
     *     3.Type string equaling "$" means the struct bound to the class
     *  e.g:
     *      "int" -> Type.Int
     *      "Runnable" -> Struct Runnable
     *      In context of class {@linkplain extension.system.SimpleFileInputStream},
     *      "$" -> The struct bound to the class {@linkplain extension.system.SimpleFileInputStream}
     *      "#extension.system.SimpleFile" -> The struct bound to the class SimpleFile,note that full name of class {@linkplain extension.system.SimpleFile}
     *      "#.SimpleFile" -> search in package extension.system(where the {@linkplain extension.system.SimpleFileInputStream} is located)
     *
     * @param typeStr the type string
     * @param s the struct
     * @param dic the token dictionary
     * @param typeTable the type table
     * @param clazz the class bound to the {@param s}
     * @param <T> the type
     * @return the type
     */
    private static <T> Type getType(String typeStr, symbols.Struct s, Dictionary dic, TypeTable typeTable, Class<T> clazz) {
        Type t;
        if (typeStr.isEmpty()) {
            t = Type.Void;
        } else if (typeStr.startsWith("#.")) {
            Package p = clazz.getPackage();
            t = LoadStruct.getBoundStructOfClass(p.getName() + "." + typeStr.substring(2));
        } else if (typeStr.startsWith("#")) {
            t = LoadStruct.getBoundStructOfClass(typeStr.substring(1));
        } else if (typeStr.equals("$")) {
            t = s;
        } else {
            t = typeTable.getType(dic.getOrReserve(typeStr));
        }

        return t;
    }

    /**
     * build a function from method m
     * @param m         the method to use
     * @param clazz     the class built from
     * @param dic       the dictionary
     * @param typeTable the type table
     * @param s         the struct
     * @param <T>       the type
     * @return the member function
     */
    private static <T> MemberFunction getMemberFunction(final Method m, final Class<T> clazz, Dictionary dic, TypeTable typeTable, symbols.Struct s) {
        StructMethod func = m.getAnnotation(StructMethod.class);
        assert func != null;
        PassThisReference tmp = m.getAnnotation(PassThisReference.class);
        final boolean needPassThisReference = tmp != null && tmp.value();
        int j = needPassThisReference ? 1 : 0;

        final int paraLength = func.args().length;
        final int argLength = paraLength + j;

        final Parameter[] ps = m.getParameters();
        assert !needPassThisReference || paraLength > 0 && (ps[0].getType() == StructValue.class || ps[0].getType() == Value.class)
                : "Annotation `" + PassThisReference.class.getSimpleName() + "' requires first parameter to be `" + Value.class.getSimpleName() + "' or `" + StructValue.class.getSimpleName() + "' in " + m;

        assert argLength == m.getParameterCount()
                : "member function `" + m + "' for '" + s.toString() + "''s parameter length not matched";
        assert !Modifier.isStatic(m.getModifiers())
                : "try to bind with static function" + m + " to " + s;

        final StackVar[] vars = new StackVar[paraLength];
        final List<Para> param = checkAndBuildParams(vars,s,m,func.args(), needPassThisReference, typeTable, dic, clazz);

        final Token funcName = func.value().isEmpty() ? dic.getOrReserve(m.getName()) : dic.getOrReserve(func.value());

        final Type returnType = getType(func.ret(), s, dic, typeTable, clazz);

        assert (returnType != null): "return type `" + func.ret() + "` for " + s + "." + funcName + " is not valid";
        assert (returnType == Type.Void || Value.class.isAssignableFrom(m.getReturnType())) ://check if the return type
                "return type `" + m.getReturnType() + "' of " + m.getName() + " is not assignable to " + Value.class.getSimpleName() + " and declared return is not void";

        //check if it is an array
        if (m.getReturnType().isInstance(ArrayValue.class)) {
            assert (returnType instanceof Array)
                    : "implementation of return type of " + m.getName() + ":can't bind type[" + returnType + "] to [" + m.getReturnType().getName() + "] ";
        }

        if (m.getReturnType().isInstance(StructValue.class)) {
            //check if it is an Struct
            assert (returnType instanceof symbols.Struct)
                    : "implementation of return type of " + m.getName() + ":can't bind type[" + returnType + "] to [" + m.getReturnType().getName() + "] ";
        }

        Stmt body = func.purevirtual() ? null
                : returnType == Type.Void ? new Stmt() {
            final StackVar arg0 = new StackVar(Word.This, s, 0, 0);

            @Override
            public void run() {
                functionBodyNoReturn((StructValue) arg0.getValue(),argLength,needPassThisReference,vars,m);
            }
        } : new Stmt() {
            final StackVar arg0 = new StackVar(Word.This, s, 0, 0);
            final boolean retFlag = func.ret().equals("$") || func.ret().startsWith("#");

            @Override
            public void run() {
                functionBodyWithReturn((StructValue) arg0.getValue(),argLength,needPassThisReference,vars,returnType,m, retFlag,s);
            }
        };
        return new MemberFunction(funcName, returnType, body, param, s);
    }

    /**
     * build a struct from class c,c must have a no arg constructor
     *
     * @param c         the class
     * @param dic       the dictionary
     * @param typeTable the type table
     * @param name      the struct name
     * @param <T>       the Type
     * @return the struct
     */
    public static <T> symbols.Struct buildStructFromClass(Class<T> c, Dictionary dic,
                                                          TypeTable typeTable, Token name, boolean needDefaultInit) {
        final Method[] methods = c.getMethods();

        symbols.Struct s = new symbols.Struct(name);
        for (final Method m : methods) {
            final StructMethod methodAnnotation = m.getAnnotation(StructMethod.class);
            if (methodAnnotation != null) {
                FunctionBasic f = getMemberFunction(m, c, dic, typeTable, s);
                if (methodAnnotation.virtual() || methodAnnotation.purevirtual()) {
                    s.defineVirtualFunction(f.name, f);
                } else {
                    s.addNaiveFunction(f.name, f);
                }
            }

            final Init initAnnotation = m.getAnnotation(Init.class);
            if (initAnnotation != null) {
                FunctionBasic f = getInitialFunction(m, c, dic, typeTable, s);
                s.defineInitialFunction(f);
            }
        }

        //extension struct from a class must have a constructor
        if (needDefaultInit && s.getInitialFunction() == null) {
            s.defineInitialFunction(getDefaultInitialFunction(c, dic, typeTable, s));
        }

        return s;
    }


    /**
     * build an initial function for s using m
     *
     * @param m         the reflected method
     * @param clazz     the class type
     * @param dic       the dictionary
     * @param typeTable the type table
     * @param s         the struct
     * @param <T>       the Class type(to bind,it must have a default constructor)
     * @return the initial function if ok or null
     */
    private static <T> InitialFunction getInitialFunction(final Method m, final Class<T> clazz, Dictionary dic, TypeTable typeTable, symbols.Struct s) {
        Init init = m.getAnnotation(Init.class);
        if (init == null) {
            return null;
        }
        PassThisReference tmp = m.getAnnotation(PassThisReference.class);
        final boolean needPassThisReference = tmp != null && tmp.value();
        int j = needPassThisReference ? 1 : 0;//goes for first parameter of implementation methods args bind for s.func arg0(always 0 or 1)
        final int paraLength = init.args().length;
        final int argLength = paraLength + j;
        final Parameter[] ps = m.getParameters();
        assert !needPassThisReference || paraLength > 0 && (ps[0].getType() == StructValue.class || ps[0].getType() == Value.class)
                : "Annotation `" + PassThisReference.class.getSimpleName() + "' requires first parameter to be `" + Value.class.getSimpleName() + "' or `" + StructValue.class.getSimpleName() + "' in " + m;
        assert argLength == m.getParameterCount() : "init function for " + s.toString() + "'s parameter length not matched";
        assert !Modifier.isStatic(m.getModifiers());


        final StackVar[] vars = new StackVar[paraLength];
        final List<Para> param = checkAndBuildParams(vars,s,m,init.args(),needPassThisReference,typeTable,dic,clazz);

        return new InitialFunction(Word.This, new Stmt() {
            final StackVar arg0 = new StackVar(Word.This, s, 0, 0);

            @Override
            public void run() {
                StructValue s1 = (StructValue) arg0.getValue();
                try {
                    s1.setExtension(clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                functionBodyNoReturn(s1,argLength,needPassThisReference,vars,m);
            }
        }, param, s);
    }


    /**
     * build a default initial function,it just construct an instance of clazz and set the s.extension
     *
     * @param clazz     the class built from
     * @param dic       the dictionary
     * @param typeTable the type table
     * @param s         the struct
     * @param <T>       the type
     * @return the default initial function
     */
    private static <T> InitialFunction getDefaultInitialFunction(final Class<T> clazz, Dictionary dic, TypeTable typeTable, symbols.Struct s) {
        final StackVar arg0 = new StackVar(Word.This, s, 0, 0);

        Stmt body = new Stmt() {
            @Override
            public void run() {
                try {
                    ((StructValue) arg0.getValue()).setExtension(clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        List<Para> param = new ArrayList<>();
        param.add(new Para(s, Word.This));
        return new InitialFunction(Word.This, body, param, s);
    }
}
