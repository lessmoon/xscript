package inter.expr;

import lexer.Num;
import lexer.Word;
import symbols.Array;
import symbols.Type;

public class ArrayVar extends Var {
    private Expr loc;
    private Expr array;

    public ArrayVar(Expr arr, Type t, Expr l) {
        super(Word.array, t);
        loc = l;
        array = arr;
        if (!(array.type instanceof Array)) {
            error("`" + array + "' is not array type");
        }
        check();
    }

    void check() {
        if (Type.max(Type.Int, loc.type) != Type.Int) {
            error("type " + loc.type + " is not valid for array");
        }

        if (loc.type != Type.Int)
            loc = ConversionFactory.getConversion(loc, Type.Int);
    }

    @Override
    public boolean isChangeable() {
        return true;
    }

    @Override
    public Expr optimize() {
        loc = loc.optimize();
        array = array.optimize();
        return this;
    }

    @Override
    public Value getValue() {
        Value c = array.getValue();
        if (c == Value.Null) {
            error("null pointer error:try to get member of a null array");
        }

        ArrayValue v = (ArrayValue) c;
        int l = ((Num) (loc.getValue()).op).value;
        if (l >= v.getSize() || l < 0) {
            error("Index " + l + " out of range( 0 ~ " + (v.getSize() - 1) + " )");
        }
        return v.getElement(l);
    }

    @Override
    public Value setValue(Value v) {
        Value c = array.getValue();
        if (c == Value.Null) {
            error("null pointer error:try to set member of a null array");
        }
        int l = ((Num) (loc.getValue()).op).value;
        ArrayValue var = (ArrayValue) c;
        if (l >= var.getSize() || l < 0) {
            error("Index " + l + " out of range( 0 ~ " + (var.getSize() - 1) + " )");
        }
        var.setElement(l, v);
        return v;
    }

    @Override
    public String shortName() {
        return toString();
    }

    @Override
    public String toString() {
        return array.toString() + "[" + loc.toString() + "]";
    }

}