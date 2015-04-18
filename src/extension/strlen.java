package extension;

import lexer.Num;
import inter.expr.Constant;
import java.util.ArrayList;

public class strlen extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant c = paras.get(0);
        return new Constant(c.toString().length());
    }
}