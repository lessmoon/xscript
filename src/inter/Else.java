package inter;

import symbols.*;
import gen.*;

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
    
    public void run(){
        if(expr.getValue() != Constant.False){
            stmt1.run();
        } else {
            stmt2.run();
        }
    }

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
    
    public void emit(BinaryCodeGen bcg){
        expr.emit(bcg);
        int x = bcg.getCurrentPosition();
        bcg.emit(CodeTag.JUMP_OP | CodeTag.JC_ZE << CodeTag.OTHER_POSITION);
        bcg.emit(new IntegerSubCode(next,new Reference<Integer>(x)));

        stmt1.emit(bcg);

        x = bcg.getCurrentPosition();
        bcg.emit(CodeTag.JUMP_OP);
        bcg.emit(new IntegerSubCode(after,new Reference<Integer>(x)));

        next.setValue(bcg.getCurrentPosition());
        stmt2.emit(bcg);
        after.setValue(bcg.getCurrentPosition());
    } 
    
    public String toString(){
        return "if(" + expr.toString() + " ){\n"
        + stmt1
        + "}else {\n" + 
        stmt2
        +"}\n";
    }
}