package vm.detail;

import vm.Instruction;
import vm.VirtualMachine;

/**
 * NullaryOperator
 */
public abstract class NullaryOperator implements Instruction {
    public BinaryOperator() {
    }

    @Override
    public void run(VirtualMachine vm) {
        operate(vm);
    }

    protected abstract void operate(VirtualMachine vm);

    public static final NullaryOperator ReturnOperator = new NullaryOperator() {
        @Override
        protected void operate(VirtualMachine vm) {
            vm.endInvokeFunction();
        }
    };
}