package inter;

import symbols.*;

class ReturnResult extends RuntimeException {
    public final Constant value;
    ReturnResult(Constant v){
        value = v;
    }
}

public class Return extends Stmt {
    public Expr expr;

    public Return(Expr e,Type t){
        expr = e;
        check(t);
    }

    public void check(Type t){
        if(t == expr.type){
            return;
        } else {
            expr = ConversionFactory.getConversion(expr,t);
            if(expr == null)
                error("return wrong type(need " + t +")");
        }
    }
    
    public void run(){
        /*
         * I *KNOW* it is wrong use of exception
         * But it works well.
         * Maybe I will change the virtual machine.
         */
        throw new ReturnResult(expr.getValue());
    }
}