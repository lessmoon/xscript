package vm.detail;

import inter.expr.Value;
import vm.Pointer;
import vm.VirtualMachine;

/**
 * NotOperator
 */
public class NotOperator extends OutputUnaryOperator {
    public NotOperator(ReadOnlyPointer input, WriteOnlyPointer target) {
        super(input, target);
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value input) {
        assert input == Value.True || input == Value.False;
        return Value.valueOf(input == Value.False);
    }
}