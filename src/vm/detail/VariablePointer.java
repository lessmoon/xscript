package vm.detail;

import inter.expr.Value;
import vm.Pointer;
import vm.Position;
import vm.VirtualMachine;

/**
 * Variable pointer
 */
class VariablePointer implements Pointer {
    public VariablePointer(Position position) {
        assert position != null;
        this.position = position;
    }

    @Override
    public Value getValue(VirtualMachine vm) {
        return vm.getValueAbsolute(position);
    }

    @Override
    public void setValue(VirtualMachine vm, Value value) {
        vm.setValueAbsolute(position, value);
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        return getClass() != obj.getClass() && position.equals(((VariablePointer) obj).position);
    }

    private final Position position;
}

