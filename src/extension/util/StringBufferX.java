package extension.util;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.Init;
import extension.annotation.StructMethod;
import inter.expr.Constant;
import inter.expr.StructConst;
import lexer.Token;
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
        
        
        @StructMethod(args={"string"})
        public void append(Constant c){
            sb.append(c.valueAs(String.class));
        }
        
        @StructMethod(args={"char"})
        public void appendCharacter(Constant c){
            sb.append(c.valueAs(Character.class));
        }
        
        @StructMethod(args={"int","int"})
        public void delete(Constant beg, Constant end){
            sb.delete(beg.valueAs(Integer.class),end.valueAs(Integer.class));
        }
        
        @StructMethod(args={"int","string"})
        public void insert(Constant beg, Constant str){
            sb.insert(beg.valueAs(Integer.class),str.valueAs(String.class));
        }
        
        @StructMethod(ret = "$")
        public StructConst reverse(){
            return new StructConst(symbols.Struct.StructPlaceHolder,new StringBufferXProxy(sb.reverse()));
        }

        @StructMethod(args={"int","char"})
        public void setCharAt(Constant i, Constant c){
            sb.setCharAt(i.valueAs(Integer.class),c.valueAs(Character.class));
        }
        
        @StructMethod(value="toString",ret="string")
        public Constant toStringX(){
            return new Constant(sb.toString());
        }
   }

    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(StringBufferXProxy.class,dic,typeTable,sname,true);
    }
}
