package inter.expr;

import lexer.*;
import symbols.*;

/*
 * new<type>;
 */
public class New extends Op {
    public New(Token tok,Struct type){
        super(tok,type);
    }

    @Override
    public String toString(){
        return "new " + type.toString();
    }
    @Override
    boolean isChangeable(){
        return true;
    }

    @Override
    public Constant getValue(){
        return new StructConst((Struct)type);
    }
}


