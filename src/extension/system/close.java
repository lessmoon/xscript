package extension.system;

import extension.Function;
import inter.expr.Value;
import lexer.Num;

import java.util.List;

public class close extends Function {
    public Value run(List<Value> paras){
        Value c = paras.get(0);
        int fid = ((Num)(c.op)).value;
        return new Value(ExFile.close(fid));
    }
}