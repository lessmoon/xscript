package extension.ui;

import extension.Function;
import inter.expr.Value;
import lexer.Num;

import java.util.List;

public class setPoint extends Function {
    @Override
    public Value run(List<Value> paras){
        Value id = paras.get(0);
        Value x = paras.get(1);
        Value y = paras.get(2);
        int iid = ((Num)(id.op)).value;
        int ix = ((Num)(x.op)).value;
        int iy = ((Num)(y.op)).value;
        return new Value(PaintPad.setPoint(iid,ix,iy));
    }
}