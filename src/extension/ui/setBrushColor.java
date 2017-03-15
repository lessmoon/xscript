package extension.ui;

import extension.Function;
import inter.expr.Value;
import lexer.Num;

import java.util.List;

public class setBrushColor extends Function {
    public Value run(List<Value> paras){
        Value r = paras.get(0);
        Value g = paras.get(1);
        Value b = paras.get(2);
        int ir = ((Num)(r.op)).value;
        int ig = ((Num)(g.op)).value;
        int ib = ((Num)(b.op)).value;

        return new Value(PaintPad.setBrushColor(ir,ig,ib));
    }
}