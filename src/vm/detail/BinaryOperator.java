package vm.detail;

import vm.Instruction;
import vm.Pointer;
import vm.VirtualMachine;

/**
 * BinaryOperator
 */
public abstract class BinaryOperator implements Instruction {
    final ReadOnlyPointer left;
    final ReadOnlyPointer right;

    public BinaryOperator(ReadOnlyPointer left, ReadOnlyPointer right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public void run(VirtualMachine vm) {
        Value leftValue = left.getValue(vm);
        Value rightValue = right.getValue(vm);
        operate(vm, leftValue, rightValue);
    }

    protected abstract void operate(VirtualMachine vm, Value left, Value right);
}