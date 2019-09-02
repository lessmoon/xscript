package vm.detail;

import inter.expr.Value;

/**
 * JumpIfOperator
 */
public class JumpIfOperator extends UnaryOperator {
    protected final JumpOperator impl;
    public JumpIfOperator(ReadOnlyPointer input, int offset) {
        super(input);
        impl = new JumpOperator(offset);
    }

    @Override
    protected void operate(VirtualMachine vm, Value value) {
        if (value != Value.False) {
            assert value == Value.True;
            impl.run(vm);
        }
    }
}