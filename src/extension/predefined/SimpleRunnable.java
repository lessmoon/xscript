package extension.predefined;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.StructMethod;
import runtime.Dictionary;
import runtime.TypeTable;

/**
 * Created by lessmoon on 2017/3/12.
 */
public class SimpleRunnable extends Struct{

    @Override
    public symbols.Struct setup(symbols.Struct struct, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(SimpleRunnableProxy.class,dic,typeTable,struct,false);
    }

    public static class SimpleRunnableProxy{
        @StructMethod(purevirtual = true,_default = true)
        public void run(){}
    }
}
