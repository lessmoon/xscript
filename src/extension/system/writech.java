package extension.system;

import lexer.Num;
import lexer.Char;
import inter.expr.Constant;
import extension.Function;

import java.util.List;

public class writech extends Function {
    public Constant run(List<Constant> paras){
        Constant c = paras.get(0);
        int fid = ((Num)(c.op)).value;
        c = paras.get(1);
        char ch = ((Char)(c.op)).value;
        return new Constant(ExFile.writech(fid,ch));
    }
}