package extension.ui;

import extension.Function;
import inter.expr.Constant;

import java.util.List;

/*
 * int addString(string str,int x,int y)
 * 
 */

public class ClearString extends Function {

    @Override
    public Constant run(List<Constant> paras){
        return new Constant(PaintPad.clearString());
    }
}