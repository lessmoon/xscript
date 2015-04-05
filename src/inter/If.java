package inter;

import symbols.*;
import gen.*;

public class If extends Stmt{
    Expr expr;
    Stmt stmt;
    public If(Expr x,Stmt s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool)
            expr.error("boolean requried in if");
    }

    public void run(){
        if(expr.getValue() != Constant.False){
            stmt.run();
        }
    }

    public Stmt optimize(){
        stmt = stmt.optimize();
        if(expr == Constant.False){/*constant False,it will never happen to run the stmt*/
            return Stmt.Null;
        } else if(expr == Constant.True){
            return stmt;
        }
        return this;
    }
   
    public void emit(BinaryCodeGen bcg){
        expr.emit(bcg);
        int x = bcg.getCurrentPosition();
        bcg.emit(CodeTag.JUMP_OP | CodeTag.JC_ZE << CodeTag.OTHER_POSITION);
        bcg.emit(new IntegerSubCode(after,new Reference<Integer>(x)));
        stmt.emit(bcg);
        after.setValue(bcg.getCurrentPosition());
    } 
}