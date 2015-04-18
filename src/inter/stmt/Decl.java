package inter.stmt;

import lexer.*;
import symbols.*;
import runtime.*;
import inter.expr.Constant;
import inter.expr.Expr;
import inter.expr.ConversionFactory;

public class Decl extends Stmt {
    Token id;
    Type  type;
    Expr  value;

    public Decl(Token i,Type t,Expr v){
        id = i;
        type = t;
        value = v;

        if(!check()){
            error("Can't assign " + value.type + " to " + id + "(" + type + ")");
        }
    }

    public boolean check(){
        if(value == null){
            return true;
        } else if( !type.equals(value.type) ) {
            value = ConversionFactory.getConversion(value,type);
            return value != null;
        }
        return true;
    }

    public void run(){
        Constant v = (value == null)?null:value.getValue();
        VarTable.pushVar(v);
    }
    
    public static Decl getDecl(Token i,Type t,Expr v){
        if(t instanceof Struct){
            return new StructDecl(i,t,v);
        } if (t instanceof Array){
            return new ArrayDecl(i,t,v);
        } else {
            return new Decl(i,t,v);
        }
    }
    
    public String toString(){
        String res = "Decl[" + type + "] " + id ;
        if(value != null){
            res += "=" + value;
        }
        return res + "\n";
    }
}