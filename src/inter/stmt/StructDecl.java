package inter.stmt;

import inter.expr.Constant;
import inter.expr.Expr;
import lexer.Token;
import runtime.VarTable;
import symbols.Struct;
import symbols.Type;

public class StructDecl extends Decl {

    public StructDecl(Token i,Type t,Expr v){
        super(i,t,v);
        if(!check(0)){
            error("Can't assign " + value.type + " to " + id + "(" + type + ")");
        }
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