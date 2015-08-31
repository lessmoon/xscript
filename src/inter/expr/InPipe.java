package inter.expr;

public class InPipe extends Expr {
    Constant    value;
    Expr        expr;

    public InPipe(Expr expr){
        super(expr.op,expr.type);
        this.expr = expr;
        value     = null;
    }

    @Override
    public boolean isChangeable(){
        return expr.isChangeable();
    }

    @Override
    public Expr optimize(){
        expr = expr.optimize();
        if(expr.isChangeable())
            return this;
        else
            return expr;
    }

    @Override
    public Constant getValue(){
        value = expr.getValue();
        return value;
    }

    public Constant getPipeValue(){
        return value;
    }
}