package extension.system;

import lexer.Num;
import inter.expr.Constant;
import extension.Function;

import java.util.List;

public class close extends Function {
    public Constant run(List<Constant> paras){
        Constant c = paras.get(0);
        int fid = ((Num)(c.op)).value;
        return new Constant(ExFile.close(fid));
    }
}