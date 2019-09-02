package vm.detail;

import inter.expr.Value;
import vm.ReadOnlyPointer;
import vm.VirtualMachine;
import vm.WriteOnlyPointer;

/**
 * Register pointer
 */
public class ImediateValuePointer implements ReadOnlyPointer {
    public ImediateValuePointer(Value value) {
        this.value = value;
    }

    @Override
    public Value getValue(VirtualMachine vm) {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        return obj != null && getClass() != obj.getClass() && value.equals(obj.value);
    }

    private final Value value;

}