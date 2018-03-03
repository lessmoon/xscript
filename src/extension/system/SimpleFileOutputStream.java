package extension.system;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.Init;
import extension.annotation.StructMethod;
import inter.expr.StructValue;
import inter.expr.Value;
import runtime.Dictionary;
import runtime.TypeTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lessmoon on 2017/3/17.
 */
public class SimpleFileOutputStream extends Struct{
    public static class SimpleFileOutputStreamProxy{
        FileOutputStream fileOutputStream;

        @Init(param = {"#.SimpleFile","bool"})
        @StructMethod(param = {"#.SimpleFile","bool"})
        public void open(StructValue file,Value append) throws IOException {
            if(fileOutputStream != null){
                fileOutputStream.close();
            }
            File f = ((SimpleFile.SimpleFileProxy) file.getExtension()).file;
            fileOutputStream  = new FileOutputStream(((SimpleFile.SimpleFileProxy) file.getExtension()).file,append.valueAs(Boolean.class));
        }

        @StructMethod(param = {"int"})
        public void writeInt(Value value) throws IOException {
            fileOutputStream.write(value.valueAs(Integer.class));
        }
        
        @StructMethod(param = {"string"})
        public void writeString(Value value) throws IOException {
            fileOutputStream.write(value.valueAs(String.class).getBytes());
        }

        @StructMethod
        public void close() throws IOException {
            fileOutputStream.close();
        }

        @StructMethod
        public void flush() throws IOException {
            fileOutputStream.flush();
        }
    }

    @Override
    public symbols.Struct setup(symbols.Struct struct, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(SimpleFileOutputStreamProxy.class,dic,typeTable,struct,true);
    }
}
