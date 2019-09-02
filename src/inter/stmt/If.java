package inter.stmt;

import org.graalvm.compiler.lir.amd64.AMD64Move.Pointer;

import inter.expr.Expr;
import inter.expr.Value;
import symbols.Type;
import vm.Compiler;
import vm.compiler.PlaceHolder;
import vm.detail.JumpIfOperator;
import vm.detail.NotOperator;

public class If extends Stmt {
    Expr expr;
    Stmt stmt;

    public If(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool)
            expr.error("boolean required in if");
    }

    @Override
    public void run() {
        if (expr.getValue() != Value.False) {
            stmt.run();
        }
    }

    @Override
    public Stmt optimize() {
        stmt = stmt.optimize();
        if (expr == Value.False) {/* constant False,it will never happen to run the body */
            return Stmt.Null;
        } else if (expr == Value.True) {
            return stmt;
        }
        return this;
    }

    @Override
    public String toString() {
        return "if (" + expr + ") {\n" + stmt + "}\n";
    }

    @Override
    public void compile(Compiler compiler) {
        Pointer condition = expr.compile(compiler);
        Pointer notCondition = compiler.acquireTemporary();
        compiler.emitInstruction(new NotOperator(condition, notCondition));
        PlaceHolder placeHolder = compiler.emitPlaceHolder();
        int begin = compiler.getProgramCounter();
        stmt.compile(compiler);
        int end = compiler.getProgramCounter();
        placeHolder.writeBack(compiler, new JumpIfOperator(notCondition, end - begin));
    }
}