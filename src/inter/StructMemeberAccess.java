package inter;

import runtime.*;
import lexer.*;
import symbols.*;

public class StructMemeberAccess extends Var {
    Expr    value;
    Token   memeber;

    public StructMemeberAccess(Expr v,Token m){
        super(Word.struct,null);
        value = v;
        memeber = m;
        check();
    }

    void check(){
        if( ! (value.type instanceof Struct) ){
            error("struct access can't be used for " + value.type );
        }
        type = ((Struct)(value.type)).getType(memeber);
        if( type == null ){
            error("Can't find memeber `" + memeber + "' in " + value.type);
        }
    }
    
    boolean isChangeable(){
        return true;
    }
    
    public Expr optimize(){
        value = value.optimize();
        return this;
    }

    public Constant getValue(){
        StructConst s = (StructConst)value.getValue();
        return s.getElement(memeber);
    }

    public Constant setValue(Constant c){
        StructConst s = (StructConst)value.getValue();
        return s.setElement(memeber,c);
    }
}