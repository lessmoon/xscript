package extension.math;

import extension.Function;
import inter.expr.Value;
import lexer.Num;

import java.util.List;

public class SetSeed extends Function {
    public Value run(List<Value> paras){
        Value seed = paras.get(0);
        int t = ((Num)(seed.op)).value;
        Random.gen.setSeed(t);
        return Value.False;
    }
}