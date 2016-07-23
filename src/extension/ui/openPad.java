package extension.ui;

import lexer.Num;
import lexer.Str;
import inter.expr.Constant;
import extension.Function;

import java.util.List;

public class openPad extends Function {
    public Constant run(List<Constant> paras){
        Constant w = paras.get(0);
        Constant h = paras.get(1);
        Constant name = paras.get(2);
        int iw = ((Num)(w.op)).value;
        int ih = ((Num)(h.op)).value;
        String sn = ((Str)(name.op)).value;
        return new Constant(PaintPad.openPad(iw,ih,sn));
    }
}