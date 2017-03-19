package extension.system;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.Init;
import extension.annotation.StructMethod;
import inter.expr.StructValue;
import inter.expr.Value;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;

import java.io.File;
import java.io.IOException;

/**
 * Created by lessmoon on 2017/3/16.
 */
public class SimpleFile extends Struct {
    public static final class SimpleFileProxy {
        File file;

        @Init(param = "string")
        public void set(Value path) {
            file = new File(path.valueAs(String.class));
        }

        @StructMethod(ret = "string")
        public Value getName(){
            return new Value(file.getName());
        }

        @StructMethod(ret = "bool")
        public Value canRead() {
            return Value.valueOf(file.canRead());
        }

        @StructMethod(ret = "bool")
        public Value canWrite() {
            return Value.valueOf(file.canWrite());
        }

        @StructMethod(ret = "bool")
        public Value isDirectory(){
            return Value.valueOf(file.isDirectory());
        }

        @StructMethod(ret = "bool")
        public Value isFile(){
            return Value.valueOf(file.isFile());
        }

        @StructMethod(ret = "bool")
        public Value isHidden(){
            return Value.valueOf(file.isHidden());
        }

        @StructMethod(ret = "bool")
        public Value exists(){
            return Value.valueOf(file.exists());
        }

        @StructMethod(ret = "bool")
        public Value mkdir(){
            return Value.valueOf(file.mkdir());
        }

        @StructMethod(ret = "bool")
        public Value createNewFile() throws IOException {
             return Value.valueOf(file.createNewFile());
        }

        //@StructMethod(ret = "$[]")
        //Value listFiles(){
            //Array a = new Array(symbols.Struct.StructPlaceHolder);
            //File[] res = file.listFiles();
            //if(res == null)
                //return Value.Null;
            //ArrayValue r = new ArrayValue(a,res.length);
            //for(int i = 0 ; i < res.length;i++) {
                //r.setElement(i, new StructValue(symbols.Struct.StructPlaceHolder, res));
           // }
            //return r;
       // }

        @StructMethod(ret = "$")
        public StructValue getParent() {
            return new StructValue(symbols.Struct.StructPlaceHolder, file.getParent());
        }
    }

    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(SimpleFileProxy.class,dic,typeTable,sname,false);
    }
}
