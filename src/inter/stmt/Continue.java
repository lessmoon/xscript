package inter.stmt;

import runtime.VarTable;
import vm.Compiler;
import vm.compiler.Placeholder;
import vm.compiler.Variable;
import vm.detail.PopStackOperator;

public class Continue extends Stmt {
    static public final Throwable ContinueCause = new Throwable();
    Stmt stmt;
    private final int sizeOfStack;

    public Continue(int s) {
        if (Stmt.Enclosing == Stmt.Null)
            error("unenclosed continue");
        stmt = Stmt.Enclosing;
        sizeOfStack = s;
    }

    @Override
    public void run() {
        /*
         * I *KNOW* it is wrong use of exception But it works well. Maybe I will change
         * the virtual machine.
         */
        for (int i = 0; i < sizeOfStack; i++)
            VarTable.popTop();

        throw new RuntimeException(ContinueCause);
    }

    @Override
    public boolean isLastStmt() {
        return true;
    }

    @Override
    public void compile(Compiler compiler) {
        if (sizeOfStack > 0) {
            compiler.emitInstruction(new PopStackOperator(sizeOfStack));
        }
        Variable<> continuePosition = compiler.getContinuePosition();
        Placeholder jump = compiler.emitPlaceholder();
        int present = compiler.getProgramCounter();
        continuePosition.addAction(pos -> {
            jump.writeBack(compiler, new JumpOperator(pos - present));
        });
    }
}