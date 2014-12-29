package inter;

import runtime.*;
import lexer.*;
import symbols.*;

public class StructMemberAccess extends Var {
    Expr    value;
    Token   member;

    public StructMemberAccess(Expr v,Token m){
        super(Word.struct,null,0,0);
        value = v;
        member = m;
        check();
    }

    void check(){
        if( ! (value.type instanceof Struct) ){
            error("struct access can't be used for " + value.type );
        }
        type = ((Struct)(value.type)).getType(member);
        if( type == null ){
            error("Can't find member `" + member + "' in " + value.type);
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
        return s.getElement(member);
    }

    public Constant setValue(Constant c){
        StructConst s = (StructConst)value.getValue();
        return s.setElement(member,c);
    }
}