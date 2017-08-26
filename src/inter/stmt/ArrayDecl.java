package inter.stmt;

import inter.expr.Expr;
import lexer.Token;
import runtime.VarTable;
import symbols.Array;
import symbols.Type;

public class ArrayDecl extends Decl {

    public ArrayDecl(Token i,Type t,Expr v){
        super(i,t,v);
        if(!check(0)){
            error("Can't assign " + value.type + " to " + id + "(" + type + ")");
        }
    }

    public boolean check(int i){
        return type instanceof Array ;
    }

    @Override
    public void run(){
        VarTable.pushVar(value.getValue());
        VarTable.defVar(id,type);
    }
}