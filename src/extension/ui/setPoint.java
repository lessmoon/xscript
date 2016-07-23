package extension.ui;

import extension.Function;
import inter.expr.Constant;
import lexer.Num;

import java.util.List;

public class setPoint extends Function {
    @Override
    public Constant run(List<Constant> paras){
        Constant id = paras.get(0);
        Constant x = paras.get(1);
        Constant y = paras.get(2);
        int iid = ((Num)(id.op)).value;
        int ix = ((Num)(x.op)).value;
        int iy = ((Num)(y.op)).value;
        return new Constant(PaintPad.setPoint(iid,ix,iy));
    }
}