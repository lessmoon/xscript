package extension.math;

import extension.Function;
import inter.expr.Value;
import lexer.Float;
import lexer.Num;

import java.util.List;

/**
 * Created by lessmoon on 2017/3/17.
 */
public class functions {
    public static class cos extends Function {
        @Override
        public Value run(List<Value> paras){
            Value theta = paras.get(0);
            float t = ((Float)(theta.op)).value;
            return new Value((float)java.lang.Math.cos(t));
        }
    }
    public static class Random extends Function {
        static final java.util.Random gen = new java.util.Random(0);
        public Value run(List<Value> paras){
            return new Value(Math.abs(gen.nextInt()));
        }
    }

    public static class SetSeed extends Function {
        public Value run(List<Value> paras){
            Value seed = paras.get(0);
            int t = ((Num)(seed.op)).value;
            Random.gen.setSeed(t);
            return Value.False;
        }
    }

    public static class sin extends Function {
        public Value run(List<Value> paras) {
            Value theta = paras.get(0);
            float t = ((Float) (theta.op)).value;
            return new Value((float) java.lang.Math.sin(t));
        }
    }
}
