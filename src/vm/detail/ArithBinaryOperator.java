package vm.detail;

import vm.Pointer;
import vm.ReadOnlyPointer;
import vm.Type;
import vm.VirtualMachine;
/**
 * ArithBinaryOperator
 */
public abstract class ArithBinaryOperator extends OutputBinaryOperator {
    protected final Type type;
    public ArithBinaryOperator(ReadOnlyPointer left, ReadOnlyPointer right, WriteOnlyPointer target, Type type) {
        super(left, right, target);
        this.type = type;
    }
}