package extension.util;

import extension.Function;
import inter.expr.Value;

import java.util.List;

public class strlen extends Function {
    public Value run(List<Value> args){
        Value c = args.get(0);
        return new Value(c.toString().length());
    }
}