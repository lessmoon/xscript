package extension.reflection;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.PassThisReference;
import extension.annotation.StructMethod;
import inter.expr.StructValue;
import inter.expr.Value;
import runtime.Dictionary;
import runtime.TypeTable;

/**
 * Created by lessmoon on 2017/4/8.
 */
public class Root extends Struct{
    public static class RootProxy{
        @PassThisReference
        @StructMethod(ret = "#.StructType")
        public Value getType(StructValue _this){
            return new StructValue(symbols.Struct.StructPlaceHolder,new StructType.StructTypeProxy((symbols.Struct) _this.type));
        }
    }

    @Override
    public symbols.Struct setup(symbols.Struct struct, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(RootProxy.class,dic,typeTable,struct,true);
    }
}
