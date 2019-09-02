package inter.expr;

import lexer.Num;
import lexer.Str;
import lexer.Word;
import symbols.Type;

public class StringAccess extends Expr {
    private Expr array;
    private Expr index;

    public StringAccess(Expr a, Expr i) {
        super(Word.array, Type.Char);
        array = a;
        index = i;
        check();
    }

    void check() {
        if (array.type != Type.Str)
            error("array access is only for " + Type.Str + "");
        if (Type.max(Type.Int, index.type) != Type.Int) {
            error("type " + index.type + " is not valid for array");
        }

        if (index.type != Type.Int)
            index = ConversionFactory.getConversion(index, Type.Int);
    }

    @Override
    public boolean isChangeable() {
        return index.isChangeable() || array.isChangeable();
    }

    @Override
    public Expr optimize() {
        index = index.optimize();
        array = array.optimize();
        if (isChangeable()) {
            return this;
        } else {
            return getValue();
        }
    }

    @Override
    public Value getValue() {
        int i = ((Num) (index.getValue().op)).value;
        String str = ((Str) (array.getValue().op)).value;
        if (i >= str.length() || i < 0) {
            error("Index " + i + " out of string range( 0 ~ " + (str.length() - 1) + " )");
        }
        return new Value(str.charAt(i));
    }
}