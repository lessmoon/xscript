package extension.system;

import extension.Function;
import inter.expr.Value;

import java.util.List;

public class open extends Function {
    public Value run(List<Value> paras){
        Value c = paras.get(0);
        return new Value(ExFile.openfile(c.toString()));
    }
}