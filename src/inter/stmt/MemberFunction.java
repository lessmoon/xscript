package inter.stmt;

import lexer.Token;
import inter.util.Param;
import symbols.Struct;
import symbols.Type;

import java.util.List;

public class MemberFunction extends StructFunction {

    public MemberFunction(Token n, Type t, List<Param> p, Struct sn) {
        super(n, t, p, sn);
    }

    public MemberFunction(Token n, Type t, List<Param> p, Struct sn, Stmt s) {
        super(n, t, p, sn, s);
    }

    @Override
    public String getDescription(boolean needStructInfo) {
        StringBuilder sb = new StringBuilder(getType().toString() + " " );
        if(needStructInfo) {
            sb.append(getStruct().lexeme).append(".");
        }
        sb.append(getName()).append("(");
        int i = 1;
        if(i < getParamList().size()){
            sb.append(getParamList().get(i++).toString());
            while(i < getParamList().size() ){
                sb.append(",");
                sb.append(getParamList().get(i++).toString());
            }
        }
        sb.append(")");
        return sb.toString();
    }
}