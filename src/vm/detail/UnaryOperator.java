package vm.detail;

import vm.Instruction;
import vm.Pointer;
import vm.VirtualMachine;

/**
 * BinaryOperator
 */
public abstract class UnaryOperator implements Instruction {
    final ReadOnlyPointer input;

    public UnaryOperator(ReadOnlyPointer input) {
        this.input = input;
    }

    @Override
    public void run(VirtualMachine vm) {
        Value value = input.getValue(vm);
        operate(vm, value);
    }

    protected abstract void operate(VirtualMachine vm, Value value);
}