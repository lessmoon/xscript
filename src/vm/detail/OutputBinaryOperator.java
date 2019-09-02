package vm.detail;

import vm.Pointer;
import vm.VirtualMachine;
import vm.WriteOnlyPointer;

/**
 * OutputBinaryOperator
 */
public abstract class OutputBinaryOperator extends BinaryOperator {
    protected final WriteOnlyPointer target;

    public OutputBinaryOperator(ReadOnlyPointer left, ReadOnlyPointer right, WriteOnlyPointer target) {
        super(left, right);
        this.target = target;
    }

    @Override
    protected void operate(VirtualMachine vm, Value left, Value right) {
        target.setValue(vm, calculate(vm, left, right));
    }

    protected abstract Value calculate(VirtualMachine vm, Value left, Value right);
}