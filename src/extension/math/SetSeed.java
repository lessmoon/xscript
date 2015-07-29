package extension.math;

import lexer.Num;
import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

public class SetSeed extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant seed = paras.get(0);    
        int t = ((Num)(seed.op)).value;
        Random.gen.setSeed(t);
        return Constant.False;
    }
}