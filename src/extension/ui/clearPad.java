package extension.ui;

import extension.Function;
import inter.expr.Value;

import java.util.List;

public class clearPad extends Function {
    public Value run(List<Value> paras){
        return new Value(PaintPad.clearPad());
    }
}