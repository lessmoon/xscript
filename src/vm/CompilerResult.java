package vm;

/**
 * CompilerResult
 */
public interface CompilerResult {
    FunctionSignature getFunctionSignature(int id);
    int addFunction(FunctionSignature signature, List<Instruction> );
}