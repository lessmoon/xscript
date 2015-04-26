package extension.ui;

import lexer.Num;
import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

public class setBrushColor extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant r = paras.get(0);
        Constant g = paras.get(1);
        Constant b = paras.get(2);   
        int ir = ((Num)(r.op)).value;
        int ig = ((Num)(g.op)).value;
        int ib = ((Num)(b.op)).value;

        return new Constant(PaintPad.setBrushColor(ir,ig,ib));
    }
}