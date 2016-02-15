package inter.stmt;

import symbols.*;
import inter.expr.Constant;
import inter.expr.Expr;

public class Else extends Stmt {
    Expr expr;
    Stmt stmt1,stmt2;
    public Else(Expr x,Stmt s1,Stmt s2){
        expr = x;
        stmt1 = s1;
        stmt2 = s2;
        if( expr.type != Type.Bool)
            expr.error("boolean required in if");
    }
    
    @Override
    public void run(){
        if(expr.getValue() != Constant.False){
            stmt1.run();
        } else {
            stmt2.run();
        }
    }

    @Override
    public Stmt optimize(){
        stmt2 = stmt2.optimize();
        if(expr == Constant.False){/*constant False,it will never happen to run the stmt2*/
            return stmt2;
        }
        stmt1 = stmt1.optimize();
        if(expr == Constant.True){/*it is always true,just remain stmt1 only*/
            return stmt1;
        }
        return this;
    }
    
	@Override
	public boolean isLastStmt(){
		return stmt1.isLastStmt()&&stmt2.isLastStmt();
	}
	
    @Override
    public String toString(){
        return "if(" + expr.toString() + " ){\n"
        + stmt1
        + "}else {\n" + 
        stmt2
        +"}\n";
    }
}