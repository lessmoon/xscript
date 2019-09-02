package inter.expr;

import lexer.Token;
import vm.Compiler;
import vm.Instruction;
import vm.Pointer;
import vm.ReadOnlyPointer;
import vm.VirtualMachine;
import vm.detail.NotOperator;

public class Not extends Logical {
    public Not(Token tok, Expr x2) {
        super(tok, x2, x2);
    }

    @Override
    public Value getValue() {
        return expr1.getValue() != Value.False ? Value.False : Value.True;
    }

    @Override
    public String toString() {
        return op.toString() + " " + expr2.toString();
    }
    
    @Override
    public Pointer compile(Compiler compiler) {
        Pointer readOnlyPointer = expr1.compile(compiler);
        //In place operator
        //FIXME: readOnlyPointer
        compiler.emitInstruction(new NotOperator(readOnlyPointer, readOnlyPointer));
    }
}