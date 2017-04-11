package inter.expr;

/**
 * Created by lessmoon on 2017/3/28.
 */

import symbols.Type;

/**
 * Make an expression const(read-only)
 */
public class ReadOnlyWrap extends Expr {
    private Expr variable;

    public ReadOnlyWrap(Expr variable) {
        this(variable,variable.type);
    }

    public ReadOnlyWrap(Expr variable, Type type){
        super(variable.op,type);
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
