package extension;

import extension.annotation.Init;
import extension.annotation.StructMethod;
import inter.expr.Constant;
import inter.expr.StackVar;
import inter.expr.StructConst;
import inter.stmt.FunctionBasic;
import inter.stmt.InitialFunction;
import inter.stmt.MemberFunction;
import inter.stmt.Stmt;
import inter.util.Para;
import lexer.Token;
import lexer.Word;
import runtime.Dictionary;
import runtime.TypeTable;
import symbols.Type;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lessmoon on 2016/8/18.
 */

public class ExtensionStructHelper {


    /**
     * build a function from method m
     * @param m the method to use
     * @param clazz the class built from
     * @param dic the dictionary
     * @param typeTable the type table
     * @param s the struct
     * @param <T> the type
     * @return the member function
     */
    public static <T> MemberFunction getMemberFunction(final Method m, final Class<T> clazz, Dictionary dic, TypeTable typeTable, symbols.Struct s) {
        StructMethod func = m.getAnnotation(StructMethod.class);
        assert(func != null);

        final int argLength = func.args().length;
        if (argLength != m.getParameterCount())
            throw new RuntimeException("member function " + m + " for " + s.toString() + "'s parameter length not matched");
        if (Modifier.isStatic(m.getModifiers()))
            throw new RuntimeException("try to bind with static function" + m + " to " + s );

        final StackVar[] vars = new StackVar[argLength];
        final List<Para> param = new ArrayList<>();

        param.add(new Para(s, Word.This));
        for (int i = 0; i < argLength; i++) {
            Type t = typeTable.getType(dic.getOrReserve(func.args()[i]));
            if (t == null)
                throw new RuntimeException("declared arg[" + (i+1) + "] of " + m.getName() + "[aka:init] type[" + func.args()[i] + "] is not found");

            Token name = dic.getOrReserve("arg#" + i);
            vars[i] = new StackVar(name, t, 0, i + 1);
            param.add(new Para(t, name));
        }
        final Token funcName = func.value().isEmpty() ?  dic.getOrReserve(m.getName()):dic.getOrReserve(func.value());

        final Type returnType = func.ret().isEmpty() ? Type.Void : typeTable.getType(dic.getOrReserve(func.ret()));

        if (returnType == null) {
            throw new RuntimeException("return type `" + func.ret() + "` for " + s + "." + funcName + " is not valid");
        } else if (returnType != Type.Void && !Constant.class.isAssignableFrom(m.getReturnType())) {//check if the return type
            throw new RuntimeException("return type `" + m.getReturnType() + "' of " + m.getName() + " is not assignable to Constant and declared return is not void");
        }
        Stmt body = returnType == Type.Void ? new Stmt() {
            final StackVar arg0 = new StackVar(Word.This, s, 0, 0);

            @Override
            public void run() {
                StructConst s1 = (StructConst) arg0.getValue();
                final Object[] args = new Constant[argLength];

                for (int i = 0; i < argLength; i++) {
                    args[i] = vars[i].getValue();
                }
                try {
                    m.invoke(s1.getExtension(), args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } : new Stmt() {
            final StackVar arg0 = new StackVar(Word.This, s, 0, 0);

            @Override
            public void run() {
                StructConst s1 = (StructConst) arg0.getValue();
                final Object[] args = new Constant[argLength];

                for (int i = 0; i < argLength; i++) {
                    args[i] = vars[i].getValue();
                }
                try {
                    Constant ret = (Constant) m.invoke(s1.getExtension(), args);

                    //check return type dynamically
                    if (!ret.type.equals(returnType)) {
                        if (returnType instanceof symbols.Struct && ret.type instanceof symbols.Struct) {
                            if (ret != Constant.Null) {
                                if (!((symbols.Struct) ret.type).isChildOf((symbols.Struct) returnType)) {
                                    throw new RuntimeException("error return type[ " + ret.type + "] expect:" + returnType);
                                }
                            }
                        } else {
                            throw new RuntimeException("error return type[ " + ret.type + "] expect:" + returnType);
                        }
                    }
                    ret(ret);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        };
        return new MemberFunction(funcName, returnType, body, param, s);
    }

    /**
     * build a struct from class c,c must have a no arg constructor
     * @param c the class
     * @param dic the dictionary
     * @param typeTable the type table
     * @param name the struct name
     * @param <T> the Type
     * @return the struct
     */
    public static <T> symbols.Struct buildStructFromClass(Class<T> c, Dictionary dic,
                                                          TypeTable typeTable, Token name,boolean needDefaultInit) {
        final Method[] methods = c.getMethods();

        symbols.Struct s = new symbols.Struct(name);
        for (final Method m : methods) {
            final StructMethod methodAnnotation = m.getAnnotation(StructMethod.class);
            if (methodAnnotation != null) {
                FunctionBasic f = getMemberFunction(m,c,dic,typeTable,s);
                if (methodAnnotation.virtual()) {
                    s.defineVirtualFunction(f.name,f);
                } else {
                    s.addNormalFunction(f.name,f);
                }
            }

            final Init initAnnotation = m.getAnnotation(Init.class);
            if (initAnnotation != null) {
                FunctionBasic f = getInitialFunction(m, c, dic, typeTable, s);
                s.defineInitialFunction(f);
            }
        }

        //extension struct from a class must have a constructor
        if(needDefaultInit && s.getInitialFunction() == null){
            s.defineInitialFunction(getDefaultInitialFunction(c,dic,typeTable,s));
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
    public static <T> InitialFunction getInitialFunction(final Method m, final Class<T> clazz, Dictionary dic, TypeTable typeTable, symbols.Struct s) {
        Init init = m.getAnnotation(Init.class);
        if (init == null) {
            return null;
        }

        final int argLength = init.args().length;
        assert (argLength == m.getParameterCount()) : "init function for " + s.toString() + "'s parameter length not matched";
        assert (!Modifier.isStatic(m.getModifiers()));

        final StackVar[] vars = new StackVar[argLength];
        final List<Para> param = new ArrayList<>();

        param.add(new Para(s, Word.This));
        for (int i = 0; i < argLength; i++) {
            Type t = typeTable.getType(dic.getOrReserve(init.args()[i]));
            assert (t != null) : "declared arg[" + (i+1) + "] of " + m.getName() + "[aka:init] type[" + init.args()[i] + "] is not found";

            Token name = dic.getOrReserve("arg#" + i);
            vars[i] = new StackVar(name, t, 0, i + 1);
            param.add(new Para(t, name));
        }

        return new InitialFunction(Word.This, new Stmt() {
            final StackVar arg0 = new StackVar(Word.This, s, 0, 0);

            @Override
            public void run() {
                StructConst s1 = (StructConst) arg0.getValue();
                final Object[] args = new Constant[argLength];

                for (int i = 0; i < argLength; i++) {
                    args[i] = vars[i].getValue();
                }

                try {
                    s1.setExtension(clazz.newInstance());
                    m.invoke(s1.getExtension(), args);
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }, param, s);
    }


    /**
     * build a default initial function,it just construct an instance of clazz and set the s.extension
     * @param clazz the class built from
     * @param dic the dictionary
     * @param typeTable the type table
     * @param s the struct
     * @param <T> the type
     * @return the default initial function
     */
    public static <T> InitialFunction getDefaultInitialFunction(final Class<T> clazz, Dictionary dic, TypeTable typeTable, symbols.Struct s){
        Stmt body = new Stmt(){
            final StackVar arg0 = new StackVar(Word.This,s,0,0);
            @Override
            public void run(){
                try {
                    ((StructConst)arg0.getValue()).setExtension(clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        List<Para> param = new ArrayList<>();
        param.add(new Para(s,Word.This));
        return new InitialFunction(Word.This,body,param,s);
    }
}
