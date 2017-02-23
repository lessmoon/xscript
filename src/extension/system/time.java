package extension.system;

import inter.expr.Constant;
import extension.Function;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class time extends Function {
    public Constant run(List<Constant> paras){
        BigInteger i = BigInteger.valueOf(new Date().getTime());
        return new Constant( i ) ;
    }
}