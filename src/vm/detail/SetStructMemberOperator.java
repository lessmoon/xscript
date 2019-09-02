package vm.detail;

import inter.expr.StructValue;
import inter.expr.Value;
import symbols.Struct;
import vm.VirtualMachine;

/**
 * SetStructMemberOperator
 * a.x = b
 */
public class SetStructMemberOperator extends BinaryOperator {
    protected final int member;
    public SetStructMemberOperator(ReadOnlyPointer left, ReadOnlyPointer right, int member) {
        super(left, right, target);
        this.member = member;
    }

    @Override
    protected void calculate(VirtualMachine vm, Value left, Value right) {
        if (left == Value.Null) {
            //TODO: null pointer of member
            vm.raiseException("null pointer");
            return;
        }
        StructValue l = (StructValue)left;
        l.setElement(member, right);
    }
}