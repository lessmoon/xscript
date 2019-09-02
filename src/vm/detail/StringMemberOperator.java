package vm.detail;

import inter.expr.Value;
import vm.VirtualMachine;

/**
 * StringMemberOperator str[x]
 */
public class StringMemberOperator extends OutputBinaryOperator {
    public StringMemberOperator(ReadOnlyPointer left, ReadOnlyPointer right, WriteOnlyPointer target) {
        super(left, right, target);
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value left, Value right) {
        String str = ((Str) (left.getValue().op)).value;
        int l = ((Num) (right.op)).value;
        if (l >= str.length() || l < 0) {
            vm.raiseException("Index " + l + " out of string range( 0 ~ " + (str.length() - 1) + " )");
            return null;
        }
        return new Value(str.charAt(l));
    }
}