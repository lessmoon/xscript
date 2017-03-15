package extension.system;

import extension.Function;
import inter.expr.Value;

import java.util.List;

public class print extends Function {
    public Value run(List<Value> paras){
        Value c = paras.get(0);
        System.out.print(c.toString());
        return Value.False;
    }
}