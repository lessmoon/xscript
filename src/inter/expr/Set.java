package inter.expr;

import lexer.Tag;
import lexer.Token;
import symbols.Type;
import vm.Compiler;
import vm.Pointer;
import vm.detail.SetArrayMemberOperator;
import vm.detail.SetOperator;

public class Set extends Expr {
    public Var id;
    public Expr expr;

    public Set(Token tok, Expr i, Expr x) {
        super(tok, null);
        if (!(i instanceof Var)) {
            if (i instanceof ReadOnlyWrap) {
                error("variable `" + i.shortName() + "' is not writable!");
            } else {
                error("Operand `" + op + "' should be used between variable and expression");
            }
            return;// avoiding warning
        }

        id = (Var) i;
        expr = x;
        type = check();
        if (type == null) {
            error("Operand `" + op + "' should be used between " + id.type + " variable and expression,actually "
                    + id.type + "=" + expr.type);
        }
        if (!expr.type.isCongruentWith(type)) {
            expr = ConversionFactory.getConversion(expr, type);
        }
    }

    Type check() {
        if (op.tag == Tag.MODASS) {
            Type mt = Type.max(id.type, expr.type);
            if (mt == Type.BigInt || mt == Type.Int || mt == Type.Char)
                return id.type;
            else
                return null;
        } else if (id.type == Type.Str) {
            if (expr.type == Type.Str)
                return Type.Str;
            else if (op.tag != Tag.ADDASS)
                return null;
            else
                return Type.Str;
        } else {
            return id.type;
        }
    }

    @Override
    public boolean isChangeable() {
        return true;
    }

    @Override
    public Expr optimize() {
        expr = expr.optimize();
        return this;
    }

    @Override
    public Value getValue() {
        Value v = expr.getValue();
        return id.setValue(v);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + id.toString() + op + expr + ")";
    }

    @Override
    public Pointer compile(Compiler compiler) {
        Pointer rightPointer = expr.compile(compiler);
        if (id instanceof StringVarAccess) {
            Pointer x = ((StringVarAccess)id).array.compile(compiler);
        } else if (id instanceof StructMemberAccess) {
            ((StructMemberAccess)id).value.compile(compiler);
        } else if (id instanceof StackVar) {
            ((StackVar)id).value.compile(compiler);
        } else if (id instanceof ArrayVar) {
            //TODO: clear temp
            Pointer loc = ((ArrayVar)id).getLoc().compile(compiler);
            //clear tmp
            Pointer array = ((ArrayVar)id).getArray().compile(compiler);
            compiler.emitInstruction(new SetArrayMemberOperator(array, rightPointer, loc));
            return rightPointer;
        } else {
            //compiler.error();
            return null;
        }
    }
}