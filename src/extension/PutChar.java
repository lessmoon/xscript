package extension;

import inter.expr.Constant;
import java.util.ArrayList;

public class PutChar extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant c = paras.get(0);
        System.out.print(c);
        return Constant.False;
    }
}