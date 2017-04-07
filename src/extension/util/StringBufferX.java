package extension.util;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.Init;
import extension.annotation.PassThisReference;
import extension.annotation.StructMethod;
import inter.expr.StructValue;
import inter.expr.Value;
import runtime.Dictionary;
import runtime.TypeTable;

/**
 * Created by lessmoon on 2017/2/24.
 */
public class StringBufferX extends Struct{

   public static class StringBufferXProxy {
        StringBuilder sb;
        
        public StringBufferXProxy(){
            
        }
        
        public StringBufferXProxy(StringBuilder sb){
            this.sb = sb;
        }
        
        @Init
        public void Init(){
            sb = new StringBuilder();
        }
        
        @PassThisReference
        @StructMethod(param ={"string"},ret = "$")
        public StructValue append(StructValue _this, Value c){
            sb.append(c.valueAs(String.class));
            return _this;
        }

        @StructMethod(param = {"int"})
        public void reserve(Value c){
            sb.ensureCapacity(c.valueAs(int.class));
        }

        @PassThisReference
        @StructMethod(param ={"char"},ret = "$")
        public StructValue appendCharacter(StructValue _this,Value c){
            sb.append(c.valueAs(Character.class));
            return _this;
        }

        @PassThisReference
        @StructMethod(param ={"int","int"},ret = "$")
        public StructValue delete(StructValue _this,Value beg, Value end){
            sb.delete(beg.valueAs(Integer.class),end.valueAs(Integer.class));
            return _this;
        }

        @PassThisReference
        @StructMethod(param ={"int","string"},ret="$")
        public StructValue insert(StructValue _this,Value beg, Value str){
            sb.insert(beg.valueAs(Integer.class),str.valueAs(String.class));
            return _this;
        }
        
        @StructMethod(ret = "$")
        public StructValue reverse(){
            return new StructValue(symbols.Struct.StructPlaceHolder,new StringBufferXProxy(sb.reverse()));
        }

        @PassThisReference
        @StructMethod(param ={"int","char"},ret="$")
        public StructValue setCharAt(StructValue _this,Value i, Value c){
            sb.setCharAt(i.valueAs(Integer.class),c.valueAs(Character.class));
            return _this;
        }

        @StructMethod(value="toString",ret="string")
        public Value toStringX(){
            return new Value(sb.toString());
        }
   }

    @Override
    public symbols.Struct setup(symbols.Struct struct, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(StringBufferXProxy.class,dic,typeTable,struct,true);
    }
}
