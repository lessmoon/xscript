package extension.system;

import inter.expr.Constant;
import extension.Function;

import java.util.Date;
import java.util.List;

public class time extends Function {
    public Constant run(List<Constant> paras){
        int i = (int)(new Date().getTime()/1000);
        return new Constant( i ) ;
    }
}