package vm.detail;

import inter.expr.ArrayValue;
import inter.expr.Value;
import vm.VirtualMachine;

/**
 * ArrayMemberOperator
 * arr[x + 1]
 */
public class ArrayMemberOperator extends OutputBinaryOperator {
    public ArrayMemberOperator(ReadOnlyPointer left, ReadOnlyPointer right, WriteOnlyPointer target) {
        super(left, right, target);
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value left, Value right) {
        if (left == Value.Null) {
            //TODO: null pointer of member
            vm.raiseException("null pointer error: try to get member of a null array");
            return null;
        }
        ArrayValue array = (ArrayValue)left;
        int l = ((Num) (right.op)).value;
        if (l >= array.getSize() || l < 0) {
            vm.raiseException("Index " + l + " out of range( 0 ~ " + (array.getSize() - 1) + " )");
            return null;
        }
        return array.getElement(l);
    }
}