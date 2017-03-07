package inter.stmt;

import inter.expr.Constant;
import inter.expr.Expr;
import symbols.Type;

public class Do extends Stmt{
    Expr expr;
    Stmt stmt;
    public Do(){
        expr = null;
        stmt = null;
    }

    public void init(Stmt s,Expr x){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool)
            expr.error("boolean required in do");
    }

    @Override
    public Stmt optimize(){
        stmt = stmt.optimize();
        /*
         * Considering that if it has a break or continue in the loop statement
         * so we couldn't replace the do-while with its statement(stmt)
         */
        /*if(expr == Constant.False){/*constant False,it will never happen to run the stmt*/
        /*    return stmt;
        }*/
        return this;
    }

    @Override
    public  String toString(){
        return "do{\n" + stmt.toString() + "}while(" + expr.toString()+");\n";
    }

    @Override
    public void run(){
        do{
            try{
                stmt.run();
            }catch(RuntimeException e){
                if(e.getCause() == Break.BreakCause)
                    break;
                else if(e.getCause() == Continue.ContinueCause)
                    continue;
                else
                    throw e;
            }
        }while(expr.getValue() != Constant.False);
    }
}