package runtime;

import lexer.Token;
import symbols.*;
import inter.util.Para;

import java.util.ArrayList;

public class LoadStruct {
    public static Struct loadStruct(String pkg,String clazzname,Token sname,Dictionary dic){
        try{
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class clazz = loader.loadClass(pkg + "." + clazzname);
            extension.Struct s = (extension.Struct)clazz.newInstance();
            return s.setup(sname,dic);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}