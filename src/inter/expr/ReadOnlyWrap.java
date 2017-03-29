package inter.expr;

/**
 * Created by lessmoon on 2017/3/28.
 */

/**
 * Make an expression const(read-only)
 */
public class ReadOnlyWrap extends Expr {
    private Expr variable;

    public ReadOnlyWrap(Expr variable) {
        super(variable.op, variable.type);
        this.variable = variable;
    }

    @Override
    public boolean isChangeable() {
        return this.variable.isChangeable();
    }

    @Override
    public Expr optimize() {
        return variable.optimize();
    }

    @Override
    public Value getValue() {
        return variable.getValue();
    }

    @Override
    public  String toString(){
        return "RO:" + variable.toString();
    }

    @Override
    public String shortName() {
        return variable.shortName();
    }
}
