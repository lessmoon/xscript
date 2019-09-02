package vm.detail;

import vm.VirtualMachine;

/**
 * JumpOperator
 */
public class JumpOperator extends NullaryOperator {
    int offset;
    public JumpOperator(int offset) {
        super();
        this.offset = offset;
    }

    public void operate(VirtualMachine vm) {
        vm.addProgramCount(offset);
    }
}