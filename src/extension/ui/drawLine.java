package extension.ui;

import extension.Function;
import inter.expr.Value;
import lexer.Num;

import java.util.List;

public class drawLine extends Function {
    public Value run(List<Value> paras){
        Value x1 = paras.get(0);
        Value y1 = paras.get(1);
        Value x2 = paras.get(2);
        Value y2 = paras.get(3);
        int ix1 = ((Num)(x1.op)).value;
        int iy1 = ((Num)(y1.op)).value;
        int ix2 = ((Num)(x2.op)).value;
        int iy2 = ((Num)(y2.op)).value;
        return new Value(PaintPad.drawLine(ix1,iy1,ix2,iy2));
    }
}