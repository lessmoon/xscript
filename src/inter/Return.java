package inter;

import symbols.*;
import runtime.*;

class ReturnResult extends RuntimeException {
    public final Constant value;
    ReturnResult(Constant v){
        value = v;
    }
}

public class Return extends Stmt {
    public Expr expr;
    final int sizeOfStack;
    public Return(Expr e,Type t,int s){
        expr = e;
        check(t);
        sizeOfStack = s;
    }

    public void check(Type t){
        if(t.equals(expr.type)){
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
        ReturnResult r = new ReturnResult(expr.getValue());
        for(int i = 0 ; i < sizeOfStack;i++)
            VarTable.popTop();
        
        throw r;
    }
}