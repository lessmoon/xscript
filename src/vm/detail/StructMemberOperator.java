package vm.detail;

import inter.expr.StructValue;
import inter.expr.Value;
import symbols.Struct;
import vm.VirtualMachine;

/**
 * StructMemberOperator a.x = b
 */
public class StructMemberOperator extends OutputUnaryOperator {
    protected final int member;

    public StructMemberOperator(ReadOnlyPointer input, WriteOnlyPointer target, int member) {
        super(left, target);
        this.member = member;
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value input) {
        if (left == Value.Null) {
            // TODO: null pointer of member
            vm.raiseException("null pointer");
            return null;
        }
        StructValue l = (StructValue) left;
        return l.getElement(member);
    }
}