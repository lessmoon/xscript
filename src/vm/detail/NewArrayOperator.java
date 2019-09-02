package vm.detail;

import inter.expr.ArrayValue;
import lexer.Num;
import symbols.Array;
import vm.ReadOnlyPointer;
import vm.VirtualMachine;
import vm.WriteOnlyPointer;

/**
 * NewArrayOperator
 */
public class NewArrayOperator extends OutputUnaryOperator {
    protected final Array type;

    public NewArrayOperator(ReadOnlyPointer input, WriteOnlyPointer target, Array type) {
        super(input, target);
        this.type = type;
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value input) {
        int sz = ((Num) (input.op)).value;
        if (sz < 0) {
            vm.raiseException("try to allocate `" + type + "' array with negative number: "+ sz);
            return null;
        }
        return new ArrayValue(type, sz);
    }

}