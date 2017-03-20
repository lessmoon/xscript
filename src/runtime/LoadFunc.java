package runtime;

import extension.Function;
import inter.stmt.ExFunction;
import inter.util.Param;
import lexer.Token;
import symbols.Type;

import java.util.List;

public class LoadFunc {
    public static ExFunction loadFunc(Type t, String pkg, String clazzname, Token fn, List<Param> pl, Dictionary dic, TypeTable typeTable){
        ClassLoader loader = ClassLoader.getSystemClassLoader();

        Class clazz;

        try{
            try {
                clazz = loader.loadClass(pkg + "." + clazzname);
            } catch (Exception ignored) {
                clazz = loader.loadClass(pkg + "$" + clazzname);
            }

            Function f = (Function)clazz.newInstance();
            f.init(dic, typeTable);
            return new ExFunction(t,fn,pl,f);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}