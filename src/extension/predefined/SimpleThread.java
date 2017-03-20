package extension.predefined;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.Init;
import extension.annotation.StructMethod;
import inter.expr.StructValue;
import inter.expr.Value;
import lexer.Token;
import runtime.Dictionary;
import runtime.Interface;
import runtime.TypeTable;
import symbols.Position;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lessmoon on 2016/7/21.
 */
public class SimpleThread extends Struct {
    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(SimpleThreadProxy.class, dic, typeTable, sname, false);
    }

    public static class SimpleThreadProxy {
        final static Position vfPos = new Position(0, 0);
        Thread thread;

        @Init(param = "#.SimpleRunnable")
        public void init(Value r) {
            StructValue runnable = (StructValue) r;

            thread = new Thread(() -> {
                try {
                    List<Value> args = new ArrayList<>();
                    Interface.invokeVirtualFunctionOfStruct(runnable, vfPos, args);
                } catch (RuntimeException ignored) {

                }
            });
        }

        @StructMethod(ret ="bool", param ="$")
        public Value equals(StructValue r){
            SimpleThreadProxy ext = (SimpleThreadProxy) r.getExtension();
            return Value.valueOf(ext.thread.equals(this.thread));
        }
        
        @StructMethod(param = {"string"})
        public void setName(Value name) {
            thread.setName(name.valueAs(String.class));
        }

        @StructMethod(ret = "string")
        public Value getName() {
            return new Value(thread.getName());
        }

        @StructMethod(ret = "bigint")
        public Value getThreadId(){
            return new Value(BigInteger.valueOf(thread.getId()));
        }

        @StructMethod(ret = "bool")
        public Value start() {
            try {
                thread.start();
            } catch (Exception e) {
                return Value.False;
            }
            return Value.True;
        }

        @StructMethod(param = "int")
        public Value join(Value time) {
            try {
                thread.join(time.valueAs(Integer.class));
            } catch (Exception e) {
                return Value.False;
            }
            return Value.True;
        }

        @StructMethod(ret = "bool")
        public Value interrupt() {
            try {
                thread.interrupt();
            } catch (Exception e) {
                return Value.False;
            }
            return Value.True;
        }
    }
}
