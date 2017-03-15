package extension.math;

import extension.Function;
import inter.expr.Value;
import lexer.Float;

import java.util.List;

public class cos extends Function {
    @Override
    public Value run(List<Value> paras){
        Value theta = paras.get(0);
        float t = ((Float)(theta.op)).value;
        return new Value((float)java.lang.Math.cos(t));
    }
}