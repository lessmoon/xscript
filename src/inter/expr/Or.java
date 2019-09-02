package inter.expr;

import lexer.Token;
import vm.Compiler;
import vm.Pointer;
import vm.detail.TemporaryVariablePointer;

public class Or extends Logical {
    public Or(Token tok, Expr x1, Expr x2) {
        super(tok, x1, x2);
    }

    @Override
    public Value getValue() {
        return expr1.getValue() != Value.False ? Value.True : expr2.getValue();
    }

    @Override
    public Pointer compile(Compiler compiler) {
        boolean[] usage = compiler.getRegisterUsage();
        TemporaryState state = compiler.getTemporaryVariableUsage();
        Pointer a = expr1.compile(compiler);
        //TODO: use JumpIfNot
        int begin = compiler.getProgramCounter();
        PlaceHolder placeHolder = compiler.emitPlaceHolder();//if a = true, jump to #region end, [write back later]
        //#region b: we should release registers a used
        compiler.setRegisterUsage(usage);
        compiler.setTemporaryVariableUsage(state);
        Pointer b = expr2.compile(compiler);
        compiler.setRegisterUsage(usage);
        compiler.setTemporaryVariableUsage(state);
        //#region end
        if (!b.equals(a)) {
            compiler.emitInstruction(new SetOperator(b, a));
        }
        if (a instanceof RegisterPointer) {
            compiler.acquireRegister(((RegisterPointer)a).getId());
        } else {
            assert a instanceof TemporaryVariablePointer;
            compiler.acquireTemporaryVariable((TemporaryVariablePointer)a);
        }
        int end = compiler.getProgramCounter();
        placeHolder.writeBack(compiler, new JumpIfOperator(a, end - begin));
    }
}