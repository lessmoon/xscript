package extension.ui;

import extension.Function;
import inter.expr.Value;
import lexer.Num;
import lexer.Str;

import java.util.List;

public class openPad extends Function {
    public Value run(List<Value> paras){
        Value w = paras.get(0);
        Value h = paras.get(1);
        Value name = paras.get(2);
        int iw = ((Num)(w.op)).value;
        int ih = ((Num)(h.op)).value;
        String sn = ((Str)(name.op)).value;
        return new Value(PaintPad.openPad(iw,ih,sn));
    }
}