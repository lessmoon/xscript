package runtime;

import lexer.Token;
import symbols.Struct;

import java.util.HashMap;
import java.util.Map;

public class LoadStruct {
    public static final Map<Class, Struct> map = new HashMap<>();

    public static Struct loadStruct(String pkg,String clazzname,Token sname,Dictionary dic,TypeTable typeTable){
        try{
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class clazz = loader.loadClass(pkg + "." + clazzname);
            if(map.containsKey(clazz)){
                throw new RuntimeException("reload struct `" + clazz.getCanonicalName() + "'");
            }
            extension.Struct s = (extension.Struct) clazz.newInstance();
            Struct struct = s.setup(sname,dic, typeTable).close();
            map.put(clazz,struct);
            return struct;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Struct getBoundStructOfClass(String clazzName){
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            return getBoundStructOfClass(loader.loadClass(clazzName));
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static Struct getBoundStructOfClass(Class clazz){
            return map.get(clazz);
    }
}