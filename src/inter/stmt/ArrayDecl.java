package inter.stmt;

import lexer.*;
import symbols.*;
import runtime.*;
import inter.expr.Expr;
import inter.expr.ArrayConst;

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
        VarTable.pushVar((value != null)?value.getValue():new ArrayConst((Array)type));
    }
}