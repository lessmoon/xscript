package extension.system;

import inter.expr.Constant;
import extension.Function;

import java.util.List;

public class PutChar extends Function {
    public Constant run(List<Constant> paras){
        Constant c = paras.get(0);
        System.out.print(c);
        return Constant.False;
    }
}