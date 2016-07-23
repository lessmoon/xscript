package extension.ui;

import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

/*
 * int addString(string str,int x,int y)
 * 
 */

public class ClearString extends Function {
    public Constant run(ArrayList<Constant> paras){
        return new Constant(PaintPad.clearString());
    }
}