package inter.expr;

import lexer.*;
import symbols.*;

public class And extends Logical {
    public And(Token tok,Expr x1,Expr x2){
        super(tok,x1,x2);
    }

    @Override
    public void emitCode(ArrayList<SerialCode> i){
        IntReference addr = new IntReference();
        expr1.emitCode(i);
        i.add(new JumpFalseCode(addr));
        expr2.emitCode(i);
        addr.setValue(i.size());
    }

    @Override
    public Constant getValue(){
        return expr1.getValue() != Constant.False?expr2.getValue():Constant.False;
    }
    
    @Override
    public void emitCode(ArrayList<SerialCode> i){
        expr1.emitCode(i);
        expr2.emitCode(i);
    }
}