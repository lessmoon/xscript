package extension.math;

import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

public class Random extends Function {
    static final java.util.Random gen = new java.util.Random(0);
    public Constant run(ArrayList<Constant> paras){
        return new Constant(Math.abs(gen.nextInt()));
    }
}