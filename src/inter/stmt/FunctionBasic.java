package inter.stmt;

import inter.util.Param;
import lexer.Token;
import symbols.Type;

import java.util.ArrayList;
import java.util.List;

public abstract class FunctionBasic extends Stmt {
    private final Type type;

    public Type getType() {
        return type;
    }

    public Token getName() {
        return name;
    }

    private final Token name;
    private final List<Param> paramList;
    private boolean used = false;

    public FunctionBasic(Token name, Type t, List<Param> p) {
        this.name = name;
        type = t;
        paramList = new ArrayList<>(p);
    }

    public Param getParamInfo(int i) {
        return getParamList().get(i);
    }

    public int getParamSize() {
        return getParamList().size();
    }

    public abstract void run();

    public abstract boolean isCompleted();

    public boolean isNotCompleted() {
        return !isCompleted();
    }

    public boolean used() {
        return used;
    }

    public void setUsed() {
        used = true;
    }

    @Override
    public String toString() {
        return this.getDescription(true);
    }

    public String getDescription(boolean needStructInfo) {
        StringBuilder sb = new StringBuilder(getType().toString());
        sb.append("  ").append(getName()).append("(");
        int i = 0;
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

    public List<Param> getParamList() {
        return paramList;
    }
}