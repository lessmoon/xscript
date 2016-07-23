package extension.system;

import extension.Function;
import inter.expr.Constant;

import java.util.List;

public class print extends Function {
    public Constant run(List<Constant> paras){
        Constant c = paras.get(0);
        System.out.print(c.toString());
        return Constant.False;
    }
}