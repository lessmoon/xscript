package vm.detail;

import inter.expr.Value;

/**
 * JumpIfNotOperator
 */
public class JumpIfNotOperator extends UnaryOperator {
    protected final JumpOperator impl;
    public JumpIfNotOperator(ReadOnlyPointer input, int offset) {
        super(input);
        impl = new JumpOperator(offset);
    }

    @Override
    protected void operate(VirtualMachine vm, Value value) {
        if (value == Value.False) {
            impl.run(vm);
        }
    }
}