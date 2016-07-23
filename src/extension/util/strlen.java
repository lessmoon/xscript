package extension.util;

import inter.expr.Constant;
import extension.Function;

import java.util.List;

public class strlen extends Function {
    public Constant run(List<Constant> paras){
        Constant c = paras.get(0);
        return new Constant(c.toString().length());
    }
}