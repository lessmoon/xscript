package extension.predefined;

import extension.Function;
import inter.expr.Constant;

import java.math.BigInteger;
import java.util.List;

public class GetThreadId extends Function {
    public Constant run(List<Constant> paras){
        return new Constant(BigInteger.valueOf(Thread.currentThread().getId()));
    }
}