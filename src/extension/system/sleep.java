package extension.system;

import extension.Function;
import inter.expr.Value;
import lexer.Num;

import java.util.List;

public class sleep extends Function {
    public Value run(List<Value> paras) {
        Value duration = paras.get(0);
        int d = ((Num)(duration.op)).value;
        try {
            Thread.sleep(d);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}