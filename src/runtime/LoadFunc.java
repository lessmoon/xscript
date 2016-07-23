package runtime;

import extension.Function;
import inter.stmt.ExFunction;
import inter.util.Para;
import lexer.Token;
import symbols.Type;

import java.util.List;

public class LoadFunc {
    public static ExFunction loadFunc(Type t, String pkg, String clazzname, Token fn, List<Para> pl, Dictionary dic, TypeTable typeTable){
        try{
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class clazz = loader.loadClass(pkg + "." + clazzname);
            Function f = (Function)clazz.newInstance();
            f.init(dic, typeTable);
            return new ExFunction(t,fn,pl,f);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}