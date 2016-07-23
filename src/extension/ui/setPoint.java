package extension.ui;

import lexer.Num;
import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

public class setPoint extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant id = paras.get(0);
        Constant x = paras.get(1);
        Constant y = paras.get(2);
        int iid = ((Num)(id.op)).value;
        int ix = ((Num)(x.op)).value;
        int iy = ((Num)(y.op)).value;
        return new Constant(PaintPad.setPoint(iid,ix,iy));
    }
}