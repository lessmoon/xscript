package inter.stmt;

import inter.util.Param;
import lexer.Token;
import symbols.Struct;
import symbols.Type;

import java.util.List;

/**
 * Created by lessmoon on 2017/3/18.
 */
public class StructFunction extends Function {
    private final Struct struct;

    public StructFunction(Token n, Type t, List<Param> p, Struct sn) {
        super(n, t, p);
        struct = sn;
    }

    public StructFunction(Token n, Type t, List<Param> p, Struct sn, Stmt s) {
        super(n, t, p, s);
        struct = sn;
    }

    public Struct getStruct() {
        return struct;
    }
}
