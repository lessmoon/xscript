package extension.system;

import extension.Function;
import inter.expr.Value;

import java.util.List;

public class flush extends Function {
    public Value run(List<Value> args) {
        System.out.flush();
        return Value.Null;
    }
}