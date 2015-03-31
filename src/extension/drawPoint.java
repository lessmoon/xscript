package extension;

import lexer.Num;
import inter.Constant;
import java.util.ArrayList;

public class drawPoint extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant x = paras.get(0);
        Constant y = paras.get(1);
        int ix = ((Num)(x.op)).value;
        int iy = ((Num)(y.op)).value;
        return new Constant(PaintPad.drawPoint(ix,iy));
    }
}