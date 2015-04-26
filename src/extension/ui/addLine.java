package extension.ui;

import lexer.Num;
import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

public class addLine extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant x1 = paras.get(0);
        Constant y1 = paras.get(1);
        Constant x2 = paras.get(2);
        Constant y2 = paras.get(3);        
        int ix1 = ((Num)(x1.op)).value;
        int iy1 = ((Num)(y1.op)).value;
        int ix2 = ((Num)(x2.op)).value;
        int iy2 = ((Num)(y2.op)).value;
        return new Constant(PaintPad.addLine(ix1,iy1,ix2,iy2));
    }
}