package inter;

import lexer.*;
import symbols.*;
import runtime.*;

public class Decl extends Stmt {
    Token id;
    Type  type;
    Expr  value;
    
    public Decl(Token i,Type t,Expr v){
        id = i;
        type = t;
        value = v;
        if(check(t,v) == null){
            error("Can't assign " + value.type + " to " + id + "(" + type + ")");
        }
    }
    
    public Type check(Type t1,Expr value){
        if(value == null)
            return t1;
        else if(t1 == value.type){
            return t1;
        } else 
            return null;
    }
    
    public void run(){
        Constant v = (value == null)?null:value.getValue();
        VarTable.getTop().pushVar(id,v);
    }
}