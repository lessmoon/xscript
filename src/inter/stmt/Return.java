package inter.stmt;

import inter.expr.ConversionFactory;
import inter.expr.Expr;
import runtime.VarTable;
import symbols.Type;
import vm.Compiler;
import vm.Pointer;
import vm.detail.NullaryOperator;
import vm.detail.PopStackOperator;
import vm.detail.ReturnOperator;
import vm.detail.SetOperator;

public class Return extends Stmt {
    protected Expr expr;
    private final int sizeOfStack;

    public Return(Expr e, Type t, int s) {
        expr = e;
        check(t);
        sizeOfStack = s;
    }

    public void check(Type t) {
        if (!t.isCongruentWith(expr.type)) {
            expr = ConversionFactory.getConversion(expr, t);
        }
    }

    @Override
    public Stmt optimize() {
        expr = expr.optimize();
        return this;
    }

    @Override
    public void run() {
        /*
         * I *KNOW* it is wrong use of exception But it works well. Maybe I will change
         * the virtual machine.
         */
        ReturnResult r = new ReturnResult(expr.getValue());
        for (int i = 0; i < sizeOfStack; i++)
            VarTable.popTop();

        throw r;
    }

    @Override
    public boolean isLastStmt() {
        return true;
    }

    @Override
    public void compile(Compiler compiler) {
        Pointer a = expr.compile(compiler);
        Pointer v = compiler.acquireRegister(0);
        if (!a.equals(v)) {
            compiler.emitInstruction(new SetOperator(a, v));
        }
        if (sizeOfStack > 0) {
            compiler.emitInstruction(new PopStackOperator(sizeOfStack));
        }
        compiler.emitInstruction(NullaryOperator.ReturnOperator);
    }


    @Override
    public String toString() {
        return "Return " + expr.toString() + "(" + sizeOfStack + ")\n";
    }
}