package extension;

import lexer.Num;
import inter.expr.Constant;
import java.util.ArrayList;

public class closePad extends Function {
    public Constant run(ArrayList<Constant> paras){
        return new Constant(PaintPad.closePad());
    }
}