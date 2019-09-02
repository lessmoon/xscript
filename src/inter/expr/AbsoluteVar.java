package inter.expr;

import lexer.Token;
import runtime.VarTable;
import symbols.Type;

public class AbsoluteVar extends StackVar {
    public AbsoluteVar(Token w, Type t, int l, int o) {
        super(w, t, l, o);
    }

    @Override
    public boolean isChangeable() {
        return true;
    }

    @Override
    public Value getValue() {
        /* stack order:top to down */
        return VarTable.getVarAbsolutely(stackLevel, stackOffset);
    }

    @Override
    public Value setValue(Value v) {
        /* stack order:top to down */
        return VarTable.setVarAbsolutely(stackLevel, stackOffset, v);
    }

    @Override
    public String toString() {
        return "$" + op + "[" + stackLevel + "," + stackOffset + "]";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) && (o instanceof AbsoluteVar);
    }

}