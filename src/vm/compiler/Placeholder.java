package vm.compiler;

import vm.Compiler;
import vm.Instruction;

/**
 * PlaceHolder
 */
public interface Placeholder extends Instruction {
    public void writeBack(Compiler compiler, Instruction instruction);
}