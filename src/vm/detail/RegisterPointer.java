package vm.detail;

import inter.expr.Value;
import vm.Pointer;
import vm.VirtualMachine;

/**
 * Register pointer
 */
public class RegisterPointer implements Pointer {
    public RegisterPointer(int id) {
        this.id = id;
    }

    @Override
    public Value getValue(VirtualMachine vm) {
        return vm.getRegister(id);
    }

    @Override
    public void setValue(VirtualMachine vm, Value value) {
        vm.setRegister(id, value);
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        return obj != null && getClass() != obj.getClass() && id == ((RegisterPointer)obj).id;
    }

    private final int id;
}