package extension.ui;

import inter.expr.Constant;
import extension.Function;

import java.util.List;

public class paint extends Function {
    public Constant run(List<Constant> paras){
        return new Constant(PaintPad.paint());
    }
}