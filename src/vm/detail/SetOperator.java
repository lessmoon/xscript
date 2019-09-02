package vm.detail;

/**
 * SetOperator
 */
public class SetOperator extends OutputUnaryOperator {
    public SetOperator(ReadOnlyPointer input, WriteOnlyPointer target) {
        super(input, target);
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value input) {
        return input;
    }
}