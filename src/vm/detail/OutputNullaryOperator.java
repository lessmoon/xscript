package vm.detail;

import vm.WriteOnlyPointer;

/**
 * OutputNullaryOperator
 */
public abstract class OutputNullaryOperator extends NullaryOperator {
    protected final WriteOnlyPointer target;
    public OutputNullaryOperator(WriteOnlyPointer target) {
        super();
        this.target = target;
    }

    @Override
    protected void operate(VirtualMachine vm) {
        target.setValue(vm, calculate(vm));
    }

    protected abstract Value calculate(VirtualMachine vm);
}