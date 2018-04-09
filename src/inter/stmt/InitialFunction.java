package inter.stmt;

import inter.util.Param;
import lexer.Token;
import symbols.Struct;
import symbols.Type;

import java.util.List;

public class InitialFunction extends StructFunction {

    public InitialFunction(Token n, List<Param> p, Struct sn) {
        super(n, Type.Void, p, sn);
    }

    public InitialFunction(Token n, List<Param> p, Struct sn, Stmt s) {
        super(n, Type.Void, p, sn, s);
    }

    @Override
    public String getDescription(boolean needStructInfo) {
        StringBuilder sb = new StringBuilder();
        if (needStructInfo) {
            sb.append(getStruct().lexeme).append(".");
        }
        sb.append("this(");
        int i = 1;
        if (i < getParamList().size()) {
            sb.append(getParamList().get(i++).toString());
            while (i < getParamList().size()) {
                sb.append(", ");
                sb.append(getParamList().get(i++).toString());
            }
        }
        sb.append(")");
        return sb.toString();

    }
}