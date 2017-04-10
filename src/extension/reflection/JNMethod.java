package extension.reflection;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.StructMethod;
import extension.reflection.JNObject.JNObjectProxy;
import inter.expr.ArrayValue;
import inter.expr.StructValue;
import inter.expr.Value;
import runtime.Dictionary;
import runtime.TypeTable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lessmoon on 2017/4/9.
 */
public class JNMethod extends Struct{
    public static class JNMethodProxy {
        public JNMethodProxy(Method method) {
            this.method = method;
        }

        public JNMethodProxy() throws NoSuchMethodException {
            method = Object.class.getMethod("toString");
        }

        final Method method;

        @StructMethod(ret = "#.JNObject", param = {"#.JNObject", "#.JNObject[]"})
        public Value invoke(StructValue v, ArrayValue args) throws InvocationTargetException, IllegalAccessException {
            if (args.getSize() == 0) {
                return new StructValue(symbols.Struct.StructPlaceHolder
                        ,new JNObjectProxy(method.invoke(((JNObjectProxy)v.getExtension()).object)));
            }
            Object[] argv = args.toList().stream()
                    .map(vv -> ((JNObjectProxy) ((StructValue) vv).getExtension()).object).toArray();
            return new StructValue(symbols.Struct.StructPlaceHolder,
                    new JNObjectProxy(method.invoke(((JNObjectProxy)v.getExtension()).object,argv)));
        }

        @StructMethod(ret = "#.JNClass")
        public Value getReturnType(){
            return new StructValue(symbols.Struct.StructPlaceHolder,new JNClass.JNClassProxy(method.getReturnType()));
        }

        @StructMethod(ret = "string")
        public Value getName(){
            return new Value(method.getName());
        }
    }

    @Override
    public symbols.Struct setup(symbols.Struct struct, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(JNMethodProxy.class,dic,typeTable,struct,true);
    }
}
