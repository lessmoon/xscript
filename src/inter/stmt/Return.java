package inter.stmt;

import symbols.*;
import runtime.*;
import inter.expr.Constant;
import inter.expr.Expr;
import inter.expr.ConversionFactory;
import inter.code.*;

import java.util.ArrayList;

public class Return extends Stmt implements SerialCode{
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
    
    @Override
    public Stmt optimize(){
        expr = expr.optimize();
        return this;
    }

    @Override
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

    @Override
    public void serially_run(RunEnv r){
        r.functionReturn();
        for(int i = 0 ; i < sizeOfStack;i++)
            VarTable.popTop();
    }

    @Override
    public void emitCode(ArrayList<SerialCode> i){
        i.add(new ExprCode(expr));
        i.add(this);
    }

    @Override
    public String toString(){
        return "Return " + expr.toString() + "(" + sizeOfStack +")\n";
    }
}