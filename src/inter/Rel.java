package inter;

import lexer.*;
import symbols.*;

import gen.*;

public abstract class Rel extends Logical {
    public Rel(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }

    public Type check(Type p1,Type p2){
        if(p1 == p2)
            return Type.Bool;
        else 
            return null;
    }
    
    protected int getRelCode(){
        switch(op.tag){
        case '>':
            return  CodeTag.C_GT;
        case '<':
            return  CodeTag.C_LS;
        case Tag.EQ:
            return  CodeTag.C_EQ;
        case Tag.NE:
            return  CodeTag.C_NE;
        case Tag.GE:
            return  CodeTag.C_GE;
        case Tag.LE:
            return  CodeTag.C_LE;
        default:
            /*error*/
            return -1;
        }
    }
}
