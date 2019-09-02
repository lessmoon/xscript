package inter.stmt;

import inter.expr.Value;
import inter.util.Node;
import runtime.VarTable;
import vm.Compiler;
import vm.detail.PopStackOperator;

public abstract class Stmt extends Node {
    public Stmt() {
    }

    public void run() {
    }

    public static final Stmt Null = new Stmt() {
        @Override
        public String toString() {
            return "NullStmt\n";
        }

        @Override
        public void compile(Compiler compiler) {

        }
    };

    public static final Stmt RecoverStack = new Stmt() {
        @Override
        public void run() {
            VarTable.popTop();
        }

        @Override
        public String toString() {
            return "RecoverStack\n";
        }

        @Override
        public void compile(Compiler compiler) {
            compiler.emitInstruction(PopStackOperator.PopOneOperator);
        }
    };
    public static final Stmt PushStack = new Stmt() {
        @Override
        public void run() {
            VarTable.pushTop();
        }

        @Override
        public String toString() {
            return "PushStack\n";
        }

        @Override
        public void compile(Compiler compiler) {
            //TODO
            //compiler.emit()
        }
    };

    public static void ret(Value c) {
        throw new ReturnResult(c);
    }

    public static void ret(boolean b) {
        ret(b ? Value.False : Value.True);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "\n";
    }

    public Stmt optimize() {
        return this;
    }

    public boolean isLastStmt() {
        return false;
    }

    public static Stmt Enclosing = Null;
    public static Stmt BreakEnclosing = Null;

    public void appendToSeq(LinkedSeq s) {
        s.appendStmt(this);
    }

    public abstract void compile(Compiler compiler);
}