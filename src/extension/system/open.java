package extension.system;

import extension.Function;
import inter.expr.Constant;

import java.util.List;

public class open extends Function {
    public Constant run(List<Constant> paras){
        Constant c = paras.get(0);
        return new Constant(ExFile.openfile(c.toString()));
    }
}