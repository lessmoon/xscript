package inter;

import lexer.*;
import symbols.*;

public class For extends Stmt {
    public Expr begin;
    public Expr condition;
    public Expr end;
    public Stmt stmt;
    public For(Expr b,Expr c,Expr e,Stmt s){
        begin = b;
        condition = c;
        end = e;
        stmt = s;
        if(check(b.type,c.type,e.type) == null){
            error("Condition should be type of bool");
        }
    }

    Type check(Type b,Type c,Type e){
        return c != Type.Bool?null:c;
    }

    public void run(){
        for(begin.getValue();condition.getValue() != Constant.False;end.getValue()){
            stmt.run();
        }
    }
}