package vm;

import inter.expr.Value;

/**
 * Pointer to variable in VirtualMachine
 */
public interface Pointer extends ReadOnlyPointer, WriteOnlyPointer {
    public Value getValue(VirtualMachine vm);
    public void setValue(VirtualMachine vm, Value value);
     
    @Override
    public default ReadOnlyPointer readOnly() {
        return this;
    }
}