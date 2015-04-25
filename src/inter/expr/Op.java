package inter.expr;

import lexer.*;
import symbols.*;

public abstract class Op extends Expr{
    public Op(Token tok,Type p){
        super(tok,p);
    }
    
    @Override
    public void emitCode(ArrayList<SerialCode> i){
        expr1.emitCode(i);
        i.add(SerialCode.Push);
        expr2.emitCode(i);
        SerialCode sc = SerialCode.getCode(op.tag,type);
        i.add(sc);
    }
}