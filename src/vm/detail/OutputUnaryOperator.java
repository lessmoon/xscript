package vm.detail;

import inter.expr.Value;
import vm.Pointer;
import vm.WriteOnlyPointer;

/**
 * OutputUnaryOperator
 */
public abstract class OutputUnaryOperator extends UnaryOperator {
    protected final WriteOnlyPointer target;
    public OutputUnaryOperator(ReadOnlyPointer input, WriteOnlyPointer target) {
        super(input);
        this.target = target;
    }

    @Override
    protected void operate(VirtualMachine vm, Value value) {
        target.setValue(vm, calculate(vm, value));
    }

    protected abstract Value calculate(VirtualMachine vm, Value input);
}