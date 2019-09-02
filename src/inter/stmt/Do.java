package inter.stmt;

import inter.expr.Expr;
import inter.expr.Value;
import symbols.Type;
import vm.Compiler;
import vm.Pointer;
import vm.compiler.Variable;
import vm.detail.JumpIfOperator;

public class Do extends Stmt {
    Expr expr;
    Stmt stmt;

    public Do() {
        expr = null;
        stmt = null;
    }

    public void init(Stmt s, Expr x) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool)
            expr.error("boolean required in do");
    }

    @Override
    public Stmt optimize() {
        stmt = stmt.optimize();
        /*
         * Considering that if it has a break or continue in the loop statement so we
         * couldn't replace the do-while with its statement(body)
         */
        /*
         * if(expr == Value.False){/*constant False,it will never happen to run the body
         */
        /*
         * return body; }
         */
        return this;
    }

    @Override
    public String toString() {
        return "do {\n" + stmt.toString() + "} while (" + expr.toString() + ");\n";
    }

    @Override
    public void run() {
        do {
            try {
                stmt.run();
            } catch (RuntimeException e) {
                if (e.getCause() == Break.BreakCause)
                    break;
                else if (e.getCause() == Continue.ContinueCause)
                    continue;
                else
                    throw e;
            }
        } while (expr.getValue() != Value.False);
    }

    @Override
    public void compile(Compiler compiler) {
        //TODO: clear tmp
        Variable<Integer> savedBreak = compiler.getBreakPosition();
        Variable<Integer> savedContinue = compiler.getContinuePosition();
        compiler.setContinuePosition(compiler.newPosition());
        compiler.setBreakPosition(compiler.newPosition());
        int begin = compiler.getProgramCounter();
        stmt.compile(compiler);
        int condition = compiler.getProgramCounter();
        compiler.getContinuePosition().setValue(condition).finished();
        Pointer p = expr.compile(compiler);
        compiler.emitInstruction(new JumpIfOperator(p, begin - compiler.getProgramCounter()));
        int end = compiler.getProgramCounter();
        compiler.getBreakPosition().setValue(end).finished();
        compiler.setBreakPosition(savedBreak);
        compiler.setContinuePosition(savedContinue);
    }
}