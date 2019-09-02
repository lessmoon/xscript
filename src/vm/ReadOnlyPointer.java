package vm;

import inter.expr.Value;

/**
 * Ready-only pointer to variable in VirtualMachine
 */
public interface ReadOnlyPointer {
    public Value getValue(VirtualMachine vm);
    public WriteOnlyPointer writeOnly();
}