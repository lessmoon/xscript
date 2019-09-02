package vm;

import inter.expr.Value;

/**
 * Write-only pointer to variable in VirtualMachine
 */
public interface WriteOnlyPointer {
    public void setValue(VirtualMachine vm, Value value);
    public ReadOnlyPointer readOnly();
}