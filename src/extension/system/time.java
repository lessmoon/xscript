package extension.system;

import extension.Function;
import inter.expr.Value;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class time extends Function {
    public Value run(List<Value> paras){
        BigInteger i = BigInteger.valueOf(new Date().getTime());
        return new Value( i ) ;
    }
}