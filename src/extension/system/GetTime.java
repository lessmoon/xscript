package extension.system;

import extension.Function;
import inter.expr.StructValue;
import inter.expr.Value;
import lexer.Token;

import java.util.Calendar;
import java.util.List;


public class GetTime extends Function {
    public static Token fname = null;
    
    @Override
    public Value run(List<Value> paras){
        Value c = paras.get(0);
        if(c == Value.Null){
            return Value.Null;
        }
		Calendar x = Calendar.getInstance();
		((StructValue)c).setElement(0,new Value(x.get(Calendar.HOUR)));
		((StructValue)c).setElement(1,new Value(x.get(Calendar.MINUTE)));
		((StructValue)c).setElement(2,new Value(x.get(Calendar.SECOND)));
        return Value.Null;
    }

}