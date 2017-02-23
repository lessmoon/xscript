package extension.predefined;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.Init;
import extension.annotation.StructMethod;
import inter.expr.Constant;
import inter.expr.StructConst;
import lexer.Token;
import runtime.Dictionary;
import runtime.Interface;
import runtime.TypeTable;
import symbols.Position;

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

        @Init(args = "Runnable")
        public void init(Constant r) {
            StructConst runnable = (StructConst) r;

            thread = new Thread(() -> {
                try {
                    List<Constant> args = new ArrayList<>();
                    args.add(runnable);
                    Interface.invokeVirtualFunctionOfStruct(runnable, vfPos, args);
                } catch (RuntimeException ignored) {

                }
            });
        }

        @StructMethod(ret = "bool")
        public Constant start() {
            try {
                thread.start();
            } catch (Exception e) {
                return Constant.False;
            }
            return Constant.True;
        }

        @StructMethod(args = "int")
        public Constant join(Constant time) {
            try {
                thread.join(time.valueAs(Integer.class));
            } catch (Exception e) {
                return Constant.False;
            }
            return Constant.True;
        }

        @StructMethod(ret = "bool")
        public Constant interrupt() {
            try {
                thread.interrupt();
            } catch (Exception e) {
                return Constant.False;
            }
            return Constant.True;
        }
    }
}
