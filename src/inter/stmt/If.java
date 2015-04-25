package inter.stmt;

import symbols.*;
import inter.expr.Constant;
import inter.expr.Expr;
import inter.code.*;

import java.util.ArrayList;

public class If extends Stmt{
    Expr expr;
    Stmt stmt;
    public If(Expr x,Stmt s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool)
            expr.error("boolean requried in if");
    }

    @Override
    public void run(){
        if(expr.getValue() != Constant.False){
            stmt.run();
        }
    }

    @Override
    public Stmt optimize(){
        stmt = stmt.optimize();
        if(expr == Constant.False){/*constant False,it will never happen to run the stmt*/
            return Stmt.Null;
        } else if(expr == Constant.True){
            return stmt;
        }
        return this;
    }
    
    @Override
    public String toString(){
        return "if(" + expr + "){\n"
                + stmt
                + "}\n";
    }
    
    @Override
    public void emitCode(ArrayList<SerialCode> i){
        i.add(new ExprCode(expr));
        i.add(new JumpFalseCode(tailaddr));
        stmt.emitCode(i);
        this.setTailAddr(i.size());
    }
}