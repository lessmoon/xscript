package runtime;

import lexer.Token;
import symbols.Struct;

public class LoadStruct {
    public static Struct loadStruct(String pkg,String clazzname,Token sname,Dictionary dic,TypeTable typeTable){
        try{
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class clazz = loader.loadClass(pkg + "." + clazzname);
            extension.Struct s = (extension.Struct)clazz.newInstance();
            return s.setup(sname,dic, typeTable);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}