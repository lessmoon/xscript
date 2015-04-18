package extension;

import lexer.Real;
import inter.expr.Constant;
import java.util.ArrayList;

public class sin extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant theta = paras.get(0);    
        float t = ((Real)(theta.op)).value;
        return new Constant((float)java.lang.Math.sin(t));
    }
}