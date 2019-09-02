package inter.expr;

import lexer.Word;
import symbols.Array;

import java.util.Arrays;
import java.util.List;

public class ArrayValue extends Value {
    public final int size;
    private final Value[] arr;

    public ArrayValue(Array t, int sz) {
        super(Word.array, t);
        size = sz;
        arr = new Value[size];
        Value val = t.of.getInitialValue();
        for (int i = 0; i < size; i++) {
            arr[i] = val;
        }
    }

    @Override
    public Value getValue() {
        return this;
    }

    public Value setElement(int i, Value c) {
        arr[i] = c;
        return c;
    }

    public Value getElement(int i) {
        return arr[i];
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean isChangeable() {
        /* array reference is not changeable */
        return true;
    }

    public List<Value> toList() {
        return Arrays.asList(this.arr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Value c : arr) {
            sb.append(" ").append(c);
        }
        sb.append(" ]");
        return sb.toString();
    }

}