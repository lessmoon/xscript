package extension.reflection;


import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.StructMethod;
import inter.expr.ArrayValue;
import inter.expr.StructValue;
import inter.expr.Value;
import inter.stmt.FunctionBasic;
import runtime.Dictionary;
import runtime.Interface;
import runtime.TypeTable;

/**
 * Created by lessmoon on 2017/4/7.
 */
public class StructType extends Struct {
    public static class StructTypeProxy{
        symbols.Struct type;

        public StructTypeProxy(){
            this.type = null;
        }

        public StructTypeProxy(symbols.Struct type){
            this.type = type;
        }

        @StructMethod(ret = "bool",param="$")
        public Value isChildOf(StructValue b){
            return Value.valueOf(type.isChildOf(((StructTypeProxy)b.getExtension()).type));
        }

        @StructMethod(ret = "bool",param="$")
        public Value equals(StructValue b){
            return Value.valueOf(type.isCongruentWith(((StructTypeProxy)b.getExtension()).type));
        }

        @StructMethod(ret = "#.Root",param = "#.Root[]")
        public Value newInstance(ArrayValue args){
            if(type == null){
                return Value.Null;
            }
            StructValue s = new StructValue(type);
            FunctionBasic initialFunction = type.getInitialFunction();

            if(initialFunction != null) {
                if(initialFunction.getParamSize() != args.getSize() + 1){
                    return Value.Null;
                }
                for(int i = 0;i < args.getSize();i++){
                    args.getElement(i).getValue().type.isCongruentWith(initialFunction.getParamInfo(i+1).type);
                }

                Interface.invokeNormalFunctionOfStruct(s, initialFunction, args.toList());
            }
            return s;
        }

        @StructMethod(ret = "string")
        public Value getName(){
            return new Value(type.getName().toString());
        }
    }

    @Override
    public symbols.Struct setup(symbols.Struct struct, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(StructTypeProxy.class,dic,typeTable,struct,true);
    }
}
