package runtime;

import lexer.*;
import symbols.*;
import extension.Function;
import inter.stmt.ExFunction;
import inter.util.Para;

import java.util.ArrayList;

public class LoadFunc {
    public static ExFunction loadFunc(Type t,Token pkg,Token fn,ArrayList<Para> pl){
        try{
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class clazz = loader.loadClass(pkg.toString() + "." + fn.toString());
            Function f = (Function)clazz.newInstance();
            return new ExFunction(t,fn,pl,f);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}