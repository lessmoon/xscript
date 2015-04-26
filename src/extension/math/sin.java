package extension.math;

import lexer.Float;
import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

public class sin extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant theta = paras.get(0);    
        float t = ((Float)(theta.op)).value;
        return new Constant((float)java.lang.Math.sin(t));
    }
}