package vm.detail;

import inter.expr.Value;
import symbols.Struct;
import vm.VirtualMachine;

/**
 * InstanceOfOperator
 */
public class InstanceOfOperator extends OutputUnaryOperator {
    protected final Struct type;
    public InstanceOfOperator(ReadOnlyPointer input, WriteOnlyPointer target, Struct type) {
        super(input, target);
        this.type = type;
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value input) {
        if (input == Value.Null) {
            vm.raiseException("try to do instance of a null pointer");
            return null;
        }
        assert input.type instanceof Struct;
        return Value.valueOf(type.isCongruentWith(input.type) 
                    || ((Struct) input.type).isChildOf(type));
    }
}