package inter.stmt;

import lexer.*;
import symbols.*;
import runtime.*;
import inter.expr.Expr;
import inter.expr.StructConst;
import inter.expr.Constant;

public class StructDecl extends Decl {

    public StructDecl(Token i,Type t,Expr v){
        super(i,t,v);
        if(!check(0)){
            error("Can't assign " + value.type + " to " + id + "(" + type + ")");
        };
    }

    /*
     * TODO:May be optimized
     */
    public boolean check(int i){
        if( value == Constant.Null )
            return type instanceof Struct;
        else
            return type.equals(value.type) || ((Struct) value.type).isChildOf((Struct)type);
    }

    @Override
    public void run(){
        VarTable.pushVar(value.getValue());
    }
}