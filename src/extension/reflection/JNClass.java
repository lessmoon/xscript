package extension.reflection;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.Init;
import extension.annotation.StructMethod;
import extension.reflection.JNMethod.JNMethodProxy;
import extension.reflection.JNObject.JNObjectProxy;
import inter.expr.ArrayValue;
import inter.expr.StructValue;
import inter.expr.Value;
import runtime.Dictionary;
import runtime.TypeTable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lessmoon on 2017/4/9.
 */
public class JNClass extends Struct {
    public static class JNClassProxy {
        static final JNClassProxy NULL = new JNClassProxy(null);
        static final StructValue VALUE_NULL = new StructValue(symbols.Struct.StructPlaceHolder, NULL);

        public JNClassProxy() {
            this.clazz = Object.class;
        }

        public JNClassProxy(Class<?> clazz) {
            this.clazz = clazz;
        }

        Class<?> clazz;

        @Init(param = "string")
        public void init(Value v) throws ClassNotFoundException {
            switch (v.toString()) {
                case "int":
                    clazz = int.class;
                    break;
                case "void":
                    clazz = void.class;
                    break;
                case "short":
                    clazz = short.class;
                    break;
                case "long":
                    clazz = long.class;
                    break;
                case "double":
                    clazz = double.class;
                    break;
                case "float":
                    clazz = float.class;
                    break;
                case "boolean":
                    clazz = boolean.class;
                    break;
                case "char":
                    clazz = char.class;
                    break;
                case "byte":
                    clazz = byte.class;
                    break;
                default:
                    clazz = Class.forName(v.toString());
            }
        }

        @StructMethod(ret = "bool")
        public Value isNull() {
            return Value.valueOf(this.clazz == null);
        }

        @StructMethod(ret = "string")
        public Value getName() {
            return new Value(clazz.getName());
        }

        @StructMethod(ret = "#.JNObject", param = "#.JNObject[]")
        public Value newInstance(ArrayValue args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            if (args.getSize() == 0) {
                return new StructValue(symbols.Struct.StructPlaceHolder, new JNObjectProxy(clazz.newInstance()));
            }
            List<Object> argv = args.toList()
                    .stream()
                    .map(v -> ((JNObjectProxy) ((StructValue) v).getExtension()).object)
                    .collect(Collectors.toList());

            Class[] params = argv.stream().map(Object::getClass).collect(Collectors.toList()).toArray(new Class[args.getSize()]);

            return new StructValue(symbols.Struct.StructPlaceHolder,
                    new JNObjectProxy(clazz.getConstructor(params).newInstance(argv.toArray())));
        }

        @StructMethod(ret = "#.JNMethod", param = {"string", "$[]"})
        public Value getMethod(Value name, ArrayValue param) throws NoSuchMethodException {
            if (param.getSize() == 0) {
                return new StructValue(symbols.Struct.StructPlaceHolder, new JNMethodProxy(clazz.getMethod(name.toString())));
            }
            Class[] params = param.toList()
                    .stream()
                    .map(v -> ((JNClassProxy) ((StructValue) v).getExtension()).clazz)
                    .collect(Collectors.toList())
                    .toArray(new Class[param.getSize()]);
            return new StructValue(symbols.Struct.StructPlaceHolder, new JNMethodProxy(clazz.getMethod(name.toString(), params)));
        }

        @StructMethod(ret = "bool", param = "$")
        public Value isAssignableFrom(StructValue v) {
            return Value.valueOf(this.clazz.isAssignableFrom(((JNClassProxy) v.getExtension()).clazz));
        }

        @StructMethod(ret = "bool", param = "$")
        public Value equals(StructValue v) {
            return Value.valueOf(this.clazz.equals(((JNClassProxy) v.getExtension()).clazz));
        }

        @StructMethod(ret = "bool", param = "#.JNObject")
        public Value isInstance(StructValue v) {
            return Value.valueOf(this.clazz.isInstance(((JNObjectProxy) v.getExtension()).object));
        }

    }

    @Override
    public symbols.Struct setup(symbols.Struct struct, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(JNClassProxy.class, dic, typeTable, struct, true);
    }
}
