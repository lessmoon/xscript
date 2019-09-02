package vm.detail;

import inter.expr.Value;
import symbols.Struct;
import vm.ReadOnlyPointer;
import vm.VirtualMachine;
import vm.WriteOnlyPointer;

/**
 * NewOperator
 */
public class NewOperator extends OutputNullaryOperator {
    Struct type;
    public NewOperator(WriteOnlyPointer target, Struct type) {
        super(target);
        this.type = type;
    }

    @Override
    protected Value calculate(VirtualMachine vm) {
        //TODO: should vm manage memory?
        return new StructValue(type);
    }
}