package vm.detail;

import inter.expr.ArrayValue;
import inter.expr.Value;
import vm.ReadOnlyPointer;
import vm.VirtualMachine;

/**
 * SetArrayMemberOperator arr[x] = b
 */
public class SetArrayMemberOperator extends BinaryOperator {
    final ReadOnlyPointer location;

    public SetArrayMemberOperator(ReadOnlyPointer left, ReadOnlyPointer right, ReadOnlyPointer location) {
        super(left, right);
        this.location = location;
    }

    @Override
    protected void calculate(VirtualMachine vm, Value left, Value right) {
        if (left == Value.Null) {
            // TODO: null pointer of member
            vm.raiseException("null pointer error: try to set member of a null array");
            return;
        }

        ArrayValue array = (ArrayValue) left;
        Value loc = location.getValue(vm);
        int l = ((Num) (loc.op)).value;
        if (l >= array.getSize() || l < 0) {
            vm.raiseException("Index " + l + " out of range( 0 ~ " + (array.getSize() - 1) + " )");
            return;
        }
        array.setElement(l, right);
    }
}