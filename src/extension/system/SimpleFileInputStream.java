package extension.system;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.Init;
import extension.annotation.StructMethod;
import inter.expr.StructValue;
import inter.expr.Value;
import runtime.Dictionary;
import runtime.TypeTable;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by lessmoon on 2017/3/17.
 */
public class SimpleFileInputStream extends Struct {
    public static class SimpleFileInputStreamProxy {
        FileInputStream fileInputStream;

        @StructMethod(param = {"#extension.system.SimpleFile"})
        @Init(param = {"#extension.system.SimpleFile"})
        public void open(StructValue file) throws IOException {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            fileInputStream = new FileInputStream(((SimpleFile.SimpleFileProxy) file.getExtension()).file);
        }

        @StructMethod(ret = "int")
        public Value readChar() throws IOException {
            return new Value(fileInputStream.read());
        }

        @StructMethod
        public void close() throws IOException {
            fileInputStream.close();
        }

        @StructMethod(ret = "int")
        public Value available() throws IOException {
            return new Value(fileInputStream.available());
        }

        @StructMethod(ret = "bigint", param = "bigint")
        public Value skip(Value size) throws IOException {
            return new Value(BigInteger.valueOf(fileInputStream.skip(size.valueAs(BigInteger.class).longValue())));
        }

    }

    @Override
    public symbols.Struct setup(symbols.Struct struct, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(SimpleFileInputStreamProxy.class,dic,typeTable,struct,true);
    }
}
