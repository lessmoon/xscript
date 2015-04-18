package extension;

import lexer.Num;
import inter.expr.Constant;
import java.util.ArrayList;

public class openPad extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant w = paras.get(0);
        Constant h = paras.get(1);
        int iw = ((Num)(w.op)).value;
        int ih = ((Num)(h.op)).value;
        return new Constant(PaintPad.openPad(iw,ih));
    }
}