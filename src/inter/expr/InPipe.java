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

	/*
	 * NOTE:if the opt option is on,while using inpipe and outpipe in a SeqExpr
	 *      it should be awared that it will not affect the result of SeqExpr
	 * e.g.:	SeqExpr(ip,op)
	 *          after opt: -> SeqExpr(ip->opt,op->opt)
	 *          if ip -> opt is still ip then it doesn't matter
	 *          or ip -> opt is not changeable so op -> opt will be the value,
	 *          it still doesn't matter
	 */
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

    @Override
    public String toString(){
        return expr.toString();
    }

    public Constant getPipeValue(){
        return value;
    }
}