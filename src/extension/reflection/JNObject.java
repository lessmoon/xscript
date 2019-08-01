package extension.reflection;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.StructMethod;
import extension.reflection.JNClass.JNClassProxy;
import inter.expr.StructValue;
import inter.expr.Value;
import runtime.Dictionary;
import runtime.TypeTable;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by lessmoon on 2017/4/9.
 */
public class JNObject extends Struct {

    public static class JNObjectProxy {
        static final JNObjectProxy TRUE = new JNObjectProxy(Boolean.TRUE), FALSE = new JNObjectProxy(Boolean.FALSE),
                NULL = new JNObjectProxy(null);

        static final StructValue VALUE_NULL = new StructValue(symbols.Struct.StructPlaceHolder, NULL),
                VALUE_FALSE = new StructValue(symbols.Struct.StructPlaceHolder, FALSE),
                VALUE_TRUE = new StructValue(symbols.Struct.StructPlaceHolder, TRUE);

        public JNObjectProxy(Object object) {
            this.object = object;
        }

        public JNObjectProxy() {
            object = new Object();
        }

        @StructMethod(ret = "#.JNClass", value = "getClass")
        public Value _getClass() {
            if (object == null) {
                return new StructValue(symbols.Struct.StructPlaceHolder, new JNClassProxy(null));
            }
            return new StructValue(symbols.Struct.StructPlaceHolder, new JNClassProxy(object.getClass()));
        }

        @StructMethod(ret = "bool")
        public Value isNull() {
            return Value.valueOf(this.object == null);
        }

        @StructMethod(ret = "$")
        public Value Null() {
            return VALUE_NULL;
        }

        @StructMethod(ret = "string", value = "toString")
        public Value _toString() {
            return new Value(String.valueOf(object));
        }

        @StructMethod(ret = "$", param = "bool")
        public Value newBoolean(Value value) {
            return value == Value.True ? VALUE_TRUE : VALUE_FALSE;
        }

        @StructMethod(ret = "$", param = "int")
        public Value newLong(Value value) {
            return new StructValue(symbols.Struct.StructPlaceHolder,
                    new JNObjectProxy(Long.valueOf(value.valueAs(Integer.class))));
        }

        @StructMethod(ret = "$", param = "int")
        public Value newInteger(Value value) {
            return new StructValue(symbols.Struct.StructPlaceHolder, new JNObjectProxy(value.valueAs(Integer.class)));
        }

        @StructMethod(ret = "$", param = "real")
        public Value newFloat(Value value) {
            return new StructValue(symbols.Struct.StructPlaceHolder, new JNObjectProxy(value.valueAs(Float.class)));
        }

        @StructMethod(ret = "$", param = "char")
        public Value newCharacter(Value value) {
            return new StructValue(symbols.Struct.StructPlaceHolder, new JNObjectProxy(value.valueAs(Character.class)));
        }

        @StructMethod(ret = "$", param = "bigint")
        public Value newBigInteger(Value value) {
            return new StructValue(symbols.Struct.StructPlaceHolder,
                    new JNObjectProxy(value.valueAs(BigInteger.class)));
        }

        @StructMethod(ret = "$", param = "bigreal")
        public Value newBigDecimal(Value value) {
            return new StructValue(symbols.Struct.StructPlaceHolder,
                    new JNObjectProxy(value.valueAs(BigDecimal.class)));
        }

        @StructMethod(ret = "$", param = "real")
        public Value newDouble(Value value) {
            return new StructValue(symbols.Struct.StructPlaceHolder, new JNObjectProxy(value.valueAs(Double.class)));
        }

        @StructMethod(ret = "$", param = "string")
        public Value newString(Value value) {
            return new StructValue(symbols.Struct.StructPlaceHolder, new JNObjectProxy(value.valueAs(String.class)));
        }

        @StructMethod(ret = "int")
        public Value castInt() {
            return new Value((Integer) object);
        }

        @StructMethod(ret = "real")
        public Value castReal() {
            return new Value((Float) object);
        }

        @StructMethod(ret = "bigreal")
        public Value castDouble() {
            return new Value(new BigDecimal((Double) object));
        }

        @StructMethod(ret = "string")
        public Value castString() {
            return new Value((String) object);
        }

        @StructMethod(ret = "bigint")
        public Value castBigInt() {
            return new Value((BigInteger) object);
        }

        @StructMethod(ret = "bigint")
        public Value castLong() {
            return new Value(BigInteger.valueOf((Long) object));
        }

        @StructMethod(ret = "bigreal")
        public Value castBigReal() {
            return new Value((BigDecimal) object);
        }

        @StructMethod(ret = "char")
        public Value castChar() {
            return new Value((Character) object);
        }

        @StructMethod(ret = "bool")
        public Value castBool() {
            return Value.valueOf((Boolean) object);
        }

        final Object object;
    }

    @Override
    public symbols.Struct setup(symbols.Struct struct, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(JNObjectProxy.class, dic, typeTable, struct, true);
    }
}
