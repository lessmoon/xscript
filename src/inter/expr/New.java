package inter.expr;

import lexer.Token;
import symbols.Struct;
import vm.Compiler;
import vm.Pointer;
import vm.detail.NewOperator;

/*
 * new<type>;
 */
public class New extends Op {
    public New(Token tok, Struct type) {
        super(tok, type);
    }

    @Override
    public String toString() {
        return "new " + type.toString();
    }

    @Override
    public boolean isChangeable() {
        return true;
    }

    @Override
    public Value getValue() {
        return new StructValue((Struct) type);
    }

    @Override
    public Pointer compile(Compiler compiler) {
        //In place operator
        Pointer p = compiler.acquireTemporary();
        compiler.emitInstruction(new NewOperator(p, (Struct) type));
        return p;
    }
}