package vm;

import inter.expr.Expr;
import inter.expr.Value;
import vm.compiler.PlaceHolder;
import vm.compiler.Variable;
import vm.detail.ImediateValuePointer;
import vm.detail.RegisterPointer;
import vm.detail.TemporaryVariablePointer;

/**
 * Compiler
 */
public interface Compiler {
    public static interface TemporaryState {}
    public int getRegisterSize();
    public RegisterPointer acquireRegister(int i);
    public boolean releaseRegister(int i);
    public boolean isRegisterUsed(int i);

    public TemporaryVariablePointer acquireTemporaryVariable();
    public boolean acquireTemporaryVariable(TemporaryVariablePointer pointer);
    public void releaseTemporaryVariable(TemporaryVariablePointer pointer);
    public TemporaryState getTemporaryVariableUsage();
    public void setTemporaryVariableUsage(TemporaryState internal);

    /**
     * acquire temporary register or values
     * @return 
     */
    public default Pointer acquireTemporary() {

        int i = nextAvailableRegister();
        if (i >= 0) {
            return acquireRegister(i);
        } else {
            return acquireTemporaryVariable();
        }
    }

    public default boolean[] getRegisterUsage() {
        boolean[] usage = new boolean[getRegisterSize()];
        for (int i = 0; i < usage.length; i++) {
            usage[i] = isRegisterUsed(i);
        }
        return usage;
    }

    public default void setRegisterUsage(boolean[] usage) {
        for (int i = 0; i < usage.length; i++) {
            releaseRegister(i);
            if (usage[i]) {
                acquireRegister(i);
            }
        }
    }

    public default int nextAvailableRegister() {
        for (int i = 0; i < getRegisterSize(); i++) {
            if (!isRegisterUsed(i)) {
                return i;
            }
        }
        return -1;
    }
    public boolean emitInstruction(Instruction instruction);
    public Placeholder emitPlaceholder();

    public int getProgramCounter();
    public List<Instruction> getInstructionBody();
    public int getFunctionIdByName(String name);
    public int getMemberFunctionIdByName(String structName, String name);
    public ImediateValuePointer registerConstantValue(Value value);

    public Variable<Integer> getBreakPosition();
    public void setBreakPosition(Variable<Integer> position);

    public Variable<Integer> getContinuePosition();
    public void setContinuePosition(Variable<Integer> position);

    public default Variable<Integer> newPosition() {
        return new Variable<>();
    }
}