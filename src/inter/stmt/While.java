package inter.stmt;

import inter.expr.Expr;
import inter.expr.Value;
import symbols.Type;

public class While extends Stmt{
    Expr expr;
    Stmt stmt;
    
    public While(){
        expr = null;
        stmt = null;
    }

    public void init(Expr x,Stmt s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool)
            expr.error("boolean required in while");
    }

    @Override
    public void run(){
        while(expr.getValue() != Value.False){
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
        }
    }

    @Override
    public Stmt optimize(){
        stmt = stmt.optimize();
        if(expr == Value.False){/*constant False,it will never happen to run the body*/
            return Stmt.Null;
        }
        return this;
    }
    
    @Override
    public String toString(){
        return "while (" + expr + ") {\n"
                +stmt
                +"}\n";
    }
}