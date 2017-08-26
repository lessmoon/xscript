package inter.stmt;

import inter.expr.Expr;
import inter.expr.Value;
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

    public boolean check(int i){
        if( value == Value.Null )
            return type instanceof Struct;
        else
            return type.isCongruentWith(value.type) || ((Struct) value.type).isChildOf((Struct)type);
    }

    @Override
    public void run(){
        VarTable.pushVar(value.getValue());
        VarTable.defVar(id,type);
    }
}