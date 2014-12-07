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

        if(!check(t,v)){
            error("Can't assign " + value.type + " to " + id + "(" + type + ")");
        }
    }
    
    public boolean check(Type t,Expr v){
        if(value == null){
            return true;
        } else if( type != value.type ) {
            value = ConversionFactory.getConversion(v,type);
            return value != null;
        }
        return true;
    }
    
    public void run(){
        Constant v = (value == null)?null:value.getValue();
        VarTable.getTop().pushVar(id,v);
    }
}