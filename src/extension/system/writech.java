package extension.system;

import extension.Function;
import inter.expr.Value;
import lexer.Char;
import lexer.Num;

import java.util.List;

public class writech extends Function {
    public Value run(List<Value> paras){
        Value c = paras.get(0);
        int fid = ((Num)(c.op)).value;
        c = paras.get(1);
        char ch = ((Char)(c.op)).value;
        return new Value(ExFile.writech(fid,ch));
    }
}