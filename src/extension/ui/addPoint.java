package extension.ui;

import extension.Function;
import inter.expr.Value;
import lexer.Num;

import java.util.List;

public class addPoint extends Function {
    public Value run(List<Value> paras){
        Value x = paras.get(0);
        Value y = paras.get(1);
        int ix = ((Num)(x.op)).value;
        int iy = ((Num)(y.op)).value;
        return new Value(PaintPad.addPoint(ix,iy));
    }
}