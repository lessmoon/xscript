package extension.math;

import lexer.Float;
import inter.expr.Constant;
import extension.Function;

import java.util.List;

public class sin extends Function {
    public Constant run(List<Constant> paras){
        Constant theta = paras.get(0);    
        float t = ((Float)(theta.op)).value;
        return new Constant((float)java.lang.Math.sin(t));
    }
}