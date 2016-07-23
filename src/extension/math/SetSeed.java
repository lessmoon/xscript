package extension.math;

import lexer.Num;
import inter.expr.Constant;
import extension.Function;

import java.util.List;

public class SetSeed extends Function {
    public Constant run(List<Constant> paras){
        Constant seed = paras.get(0);    
        int t = ((Num)(seed.op)).value;
        Random.gen.setSeed(t);
        return Constant.False;
    }
}