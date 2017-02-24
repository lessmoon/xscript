package extension.predefined;

import extension.ExtensionStructHelper;
import extension.annotation.StructMethod;
import inter.expr.Constant;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;
import symbols.Struct;


public class EventCallback extends extension.Struct {
    public static class CallBack{
        @StructMethod(args={"int"},ret="bool",purevirtual=true)
        public Constant callback(Constant id){
            return Constant.False;
        }
    }

    @Override
    public Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(CallBack.class,dic,typeTable,sname,false);
    }
}