package extension.ui;

import lexer.Num;
import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

public class clearPad extends Function {
    public Constant run(ArrayList<Constant> paras){
        return new Constant(PaintPad.clearPad());
    }
}