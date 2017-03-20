package extension.system;

import extension.Function;
import inter.expr.Value;

import java.util.List;

public class PutChar extends Function {
    public Value run(List<Value> args){
        Value c = args.get(0);
        System.out.print(c);
        return Value.False;
    }
}