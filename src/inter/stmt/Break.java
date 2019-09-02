package inter.stmt;

import runtime.VarTable;
import vm.Compiler;
import vm.compiler.Placeholder;
import vm.compiler.Variable;
import vm.detail.JumpOperator;
import vm.detail.PopStackOperator;

public class Break extends Stmt {
    static public final Throwable BreakCause = new Throwable();
    Stmt stmt;
    private final int sizeOfStack;

    public Break(int s) {
        if (Stmt.BreakEnclosing == Stmt.Null)
            error("unenclosed break");
        stmt = Stmt.BreakEnclosing;
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

        throw new RuntimeException(BreakCause);
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
        Variable<> breakPosition = compiler.getBreakPosition();
        Placeholder jump = compiler.emitPlaceholder();
        int present = compiler.getProgramCounter();
        breakPosition.addAction(pos -> {
            jump.writeBack(compiler, new JumpOperator(pos - present));
        });
    }
}