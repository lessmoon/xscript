package vm.detail;

import inter.expr.Value;
import lexer.Char;
import lexer.Num;
import lexer.Str;
import vm.ReadOnlyPointer;
import vm.VirtualMachine;
import vm.WriteOnlyPointer;

/**
 * SetStringMemberOperator arr[x] = 'b'
 * Element of string value should not be changed.
 */
public class SetStringMemberOperator extends OutputBinaryOperator {
    public SetStringMemberOperator(ReadOnlyPointer left, ReadOnlyPointer right, WriteOnlyPointer target) {
        super(left, right, target);
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value left, Value right) {
        int i = ((Num) (right.getValue().op)).value;
        String str = ((Str) (left.getValue().op)).value;
        StringBuilder sb = new StringBuilder(str);
        if (i >= str.length() || i < 0) {
            error("Index " + i + " out of string range( 0 ~ " + (str.length() - 1) + " )");
        }
        sb.setCharAt(i, ((Char) (c.op)).value);
        return new Value(sb.toString());
    }
}