package extension.ui;

import extension.Function;
import inter.expr.Constant;

import java.util.List;

public class clearPad extends Function {
    public Constant run(List<Constant> paras){
        return new Constant(PaintPad.clearPad());
    }
}