package inter;

import lexer.*;
import symbols.*;
import runtime.*;

public class ArrayDecl extends Decl {

    public ArrayDecl(Token i,Type t,Expr v){
        super(i,t,v);
        if(!check(0)){
            error("Can't assign " + value.type + " to " + id + "(" + type + ")");
        };
    }

    public boolean check(int i){
        return type instanceof Array ;
    }

    public void run(){
        VarTable.getTop().pushVar(id,new ArrayConst((Array)type));
    }
}