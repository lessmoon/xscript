package inter.stmt;

import inter.expr.Expr;
import inter.expr.Value;
import symbols.Type;
import vm.Compiler;
import vm.Pointer;
import vm.compiler.Placeholder;
import vm.compiler.Variable;
import vm.detail.JumpIfOperator;
import vm.detail.JumpOperator;

public class For extends Stmt {
    private Stmt begin = null;
    private Expr condition = null;
    private Stmt end = null;
    public Stmt stmt = null;

    public For() {
    }

    public void init(Stmt b, Expr c, Stmt e, Stmt s) {
        begin = b;
        condition = c;
        end = e;
        stmt = s;
        if (check(c.type) == null) {
            error("Condition's type should be  bool");
        }
    }

    private Type check(Type c) {
        return c != Type.Bool ? null : c;
    }

    @Override
    public void run() {
        for (begin.run(); condition.getValue() != Value.False; end.run()) {
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
        }
    }

    @Override
    public Stmt optimize() {
        begin = begin.optimize();
        if (condition == Value.False) {
            // just remain the begin,and the condition
            return begin;
        } else if (condition == Value.True) {/* TODO */

        }
        end = end.optimize();
        stmt = stmt.optimize();
        return this;
    }

    /**
     * TODO: 2017/3/10 optimize to unfolding the loop
     */
    @Override
    public void appendToSeq(LinkedSeq s) {
        super.appendToSeq(s);
        // begin.appendToSeq(s);
        // new ExprStmt(condition).appendToSeq(s);
        // body.appendToSeq(s);
        // end.appendToSeq(s);
    }

    @Override
    public String toString() {
        return "for(" + begin + ";" + condition + ";" + end + "){\n" + stmt + "}\n";
    }

    @Override
    public void compile(Compiler compiler) {
        // TODO: clear tmp
        Variable<Integer> savedContinue = compiler.getContinuePosition();
        compiler.setContinuePosition(compiler.newPosition());
        Variable<Integer> savedBreak = compiler.getBreakPosition();
        compiler.setBreakPosition(compiler.newPosition());
        begin.compile(compiler);
        int conditionPosition = compiler.getProgramCounter();
        Pointer breakIfNot = null;
        Placeholder conditionPlaceHolder = null;
        if (condition != Value.True) {
            breakIfNot = condition.compile(compiler);
            conditionPlaceHolder = compiler.emitPlaceholder();
        }
        stmt.compile(compiler);
        compiler.getContinuePosition().setValue(compiler.getProgramCounter()).finished();
        end.compile(compiler);
        compiler.emitInstruction(new JumpOperator(conditionPosition - compiler.getProgramCounter()));
        compiler.getBreakPosition().setValue(compiler.getProgramCounter()).finished();
        if (conditionPlaceHolder != null && breakIfNot != null) {
            conditionPlaceHolder.writeBack(compiler,
                    new JumpIfNotOperator(breakIfNot, compiler.getProgramCounter() - conditionPosition));
        }
        compiler.setBreakPosition(savedBreak);
        compiler.setContinuePosition(savedContinue);
    }
}