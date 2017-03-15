package extension.ui;

import extension.Function;
import inter.expr.Value;

import java.util.List;

/*
 * int addString(string str,int x,int y)
 * 
 */

public class ClearString extends Function {

    @Override
    public Value run(List<Value> paras){
        return new Value(PaintPad.clearString());
    }
}