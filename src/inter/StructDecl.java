package inter;

import lexer.*;
import symbols.*;
import runtime.*;

public class StructDecl extends Decl {

    public StructDecl(Token i,Type t,Expr v){
        super(i,t,v);
        if(!check(0)){
            error("Can't assign " + value.type + " to " + id + "(" + type + ")");
        };
    }

    public boolean check(int i){
        if( value == null )
            return type instanceof Struct;
        else
            return type.equals(value.type);
    }

    public void run(){
        VarTable.getTop().pushVar(id,value != null ? value.getValue():new StructConst((Struct)type));
    }
}