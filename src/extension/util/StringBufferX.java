package extension.util;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.StructMethod;
import inter.expr.Constant;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;
import symbols.Type;

/**
 * Created by lessmoon on 2017/2/24.
 */
public class StringBufferX extends Struct{

   public static class StringBufferXProxy {
        StringBuffer sb;
        
        public StringBufferXProxy(){
            
        }
        
        public StringBufferXProxy(StringBuffer sb){
            this.sb = sb;
        }
        
        @Init
        void Init(){
            sb = new StringBuffer();
        }
        
        
        @StructMethod(args={"string"})
        void append(Content c){
            sb.append(c.valueOf(String.class));
        }
        
        @StructMethod(args={"char"})
        void appendCharacter(Content c){
            sb.append(c.valueOf(Character.class));
        }
        
        @StructMethod(args={"int","int"})
        void delete(Content beg,Content end){
            sb.delete(beg.valueOf(Integer.class),end.valueOf(Integer.class));
        }
        
        @StructMethod(args={"int","string"})
        void insert(Content beg,Content str){
            sb.insert(beg.valueOf(Integer.class),str.valueOf(String.class));
        }
        
        @StructMethod(ret = "$")
        StructConst reverse(){
            return new StructConst(Type.StructPlaceHolder,new StringBufferXProxy(sb.reverse()));
        }

        @StructMethod(args={"int","char"})
        void setCharAt(Constant i,Constant c){
            sb.setCharAt(i.valueOf(Integer.class),c.valueOf(Character.class));
        }
        
        @StructMethod(value="toString",ret="string")
        Constant toStringX(){
            return new Constant(sb.toString());
        }
   }

    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(StringBufferXProxy.class,dic,typeTable,sname,true);
    }
}
