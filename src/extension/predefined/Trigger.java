package extension.predefined;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.StructMethod;
import inter.expr.Constant;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;

/**
 * Created by lessmoon on 2017/2/24.
 */
public class Trigger extends Struct {

    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(TriggerProxy.class, dic, typeTable, sname, true);
    }

    public static class TriggerProxy {
        final Object trigger = new Object();

        @StructMethod(ret = "bool", value = "wait")
        public Constant await() {
            synchronized (trigger) {
                try {
                    trigger.wait();
                } catch (InterruptedException e) {
                    return Constant.False;
                }
            }
            return Constant.True;
        }

        @StructMethod("triggerAll")
        public void triggerAll() {
            synchronized (trigger) {
                trigger.notifyAll();
            }
        }
    }

}
