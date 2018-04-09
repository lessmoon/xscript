package symbols;

import inter.expr.Value;

public class EnvEntry {
    public final Type       type;
    public final int        stacklevel;
    public final int        offset;
    public final boolean    isReadOnly;
    public final Value      initValue;

    public EnvEntry(Type type, int stacklevel, int offset, boolean isReadOnly, Value initValue) {
        this.type = type;
        this.stacklevel = stacklevel;
        this.offset = offset;
        this.isReadOnly = isReadOnly;
        this.initValue = initValue;
    }

    public EnvEntry(Type t,int sl,int o){
        this(t,sl,o,false, null);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[").append(offset).append("]").append(type);
        if(isReadOnly){
            sb.append("[RO]");
        }
        return sb.toString();
    }
}