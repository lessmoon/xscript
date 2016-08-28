package extension.system;

import lexer.Num;
import inter.expr.Constant;
import extension.Function;

import java.util.List;

public class sleep extends Function {
    public Constant run(List<Constant> paras) {
        Constant duration = paras.get(0);    
        int d = ((Num)(duration.op)).value;
        try {
            Thread.sleep(d);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}