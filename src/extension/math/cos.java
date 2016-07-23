package extension.math;

import extension.Function;
import inter.expr.Constant;
import lexer.Float;

import java.util.List;

public class cos extends Function {
    @Override
    public Constant run(List<Constant> paras){
        Constant theta = paras.get(0);    
        float t = ((Float)(theta.op)).value;
        return new Constant((float)java.lang.Math.cos(t));
    }
}