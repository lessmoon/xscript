package inter;

import lexer.*;
import symbols.*;
import gen.*;

public class For extends Stmt {
    public Stmt begin = null;
    public Expr condition = null;
    public Stmt end = null;
    public Stmt stmt = null;
    public For(){}

    public void init(Stmt b,Expr c,Stmt e,Stmt s){
        begin = b;
        condition = c;
        end = e;
        stmt = s;
        if(check(c.type) == null){
            error("Condition's type should be  bool");
        }
    }
    
    Type check(Type c){
        return c != Type.Bool?null:c;
    }

    public void run(){
        for(begin.run();
        condition.getValue() != Constant.False;
        end.run()){
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

    public Stmt optimize(){
        begin = begin.optimize();
        if(condition == Constant.False){
            //just remain the begin,and the condition
            return begin;
        } else if(condition == Constant.True){/*TODO*/
            
        }
        end = end.optimize();
        stmt = stmt.optimize();
        return this;
    }

    void emitBinaryCode(BinaryCodeGen bcg){
        begin.emit(bcg);
        int c = bcg.getCurrentPosition();
        condition.emit(bcg);
        int e = bcg.getCurrentPosition();
        bcg.emit(CodeTag.JUMP_OP | CodeTag.JC_NE << CodeTag.OTHER_POSITION);
        bcg.emit(new IntegerSubCode(after,new Reference<Integer>(e)));//Should be offset//
        stmt.emit(bcg);
        int p = bcg.getCurrentPosition();
        next.setValue(p);
        end.emit(bcg);
        bcg.emit(CodeTag.JUMP_OP);
        bcg.emit(new IntegerSubCode(after,new Reference<Integer>(p)));
        after.setValue(bcg.getCurrentPosition());
    }

}