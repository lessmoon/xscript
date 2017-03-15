package extension.math;

import extension.Function;
import inter.expr.Value;

import java.util.List;

public class Random extends Function {
    static final java.util.Random gen = new java.util.Random(0);
    public Value run(List<Value> paras){
        return new Value(Math.abs(gen.nextInt()));
    }
}