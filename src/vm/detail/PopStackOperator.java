package vm.detail;

import vm.VirtualMachine;

/**
 * PopStackOperator
 */
public class PopStackOperator extends NullaryOperator {
    private final int size;
    public PopStackOperator(int size) {
        super();
        this.size = size;
    }

    @Override
    public void operate(VirtualMachine vm) {
        for (int i = 0; i < size; i++) {
            vm.popStackFrame();
        }
    }

    public static final PopStackOperator PopOneOperator = new PopStackOperator(1);
}