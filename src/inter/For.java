package inter;

import lexer.*;
import symbols.*;

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
}