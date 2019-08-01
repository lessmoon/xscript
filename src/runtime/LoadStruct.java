package runtime;

import lexer.Token;
import symbols.Struct;

import java.util.HashMap;
import java.util.Map;

public enum LoadStruct {
    ;
    public static final Map<Class<?>, Struct> map = new HashMap<>();

    public static Struct loadStruct(String pkg, String clazzname, Token sname, Dictionary dic, TypeTable typeTable) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Class<?> clazz;
        try {
            try {
                clazz = loader.loadClass(pkg + "." + clazzname);
            } catch (Exception ignored) {
                clazz = loader.loadClass(pkg + "$" + clazzname);
            }

            Struct struct = map.get(clazz);
            if (struct != null && struct.isClosed()) {
                throw new RuntimeException("reload struct `" + clazz.getCanonicalName() + "'");
            }

            extension.Struct s = (extension.Struct) clazz.getDeclaredConstructor().newInstance();
            struct = struct != null ? s.setup(struct, dic, typeTable) : s.setup(sname, dic, typeTable);
            map.put(clazz, struct.close());
            return struct;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Struct preDeclare(String pkg, String clazzName, Token sname, Dictionary dic, TypeTable typeTable) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Class<?> clazz;

        try {
            try {
                clazz = loader.loadClass(pkg + "." + clazzName);
            } catch (Exception ignored) {
                clazz = loader.loadClass(pkg + "$" + clazzName);
            }

            Struct struct = map.get(clazz);
            if (struct == null) {
                struct = new Struct(sname);
                map.put(clazz, struct);
            } else if (struct.getName() != sname) {
                throw new RuntimeException("reload struct `" + clazz.getCanonicalName() + "'");
            }

            return struct;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Struct getBoundStructOfClass(String pkg, String clazzName) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        Class<?> clazz;
        try {
            clazz = loader.loadClass(pkg + "." + clazzName);
        } catch (Exception ignored) {
            try {
                clazz = loader.loadClass(pkg + "$" + clazzName);
            } catch (Exception e) {
                return null;
            }
        }
        return getBoundStructOfClass(clazz);
    }

    public static Struct getBoundStructOfClass(String clazzName) {
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            return getBoundStructOfClass(loader.loadClass(clazzName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Struct getBoundStructOfClass(Class<?> clazz) {
        return map.get(clazz);
    }
}