package extension;

import lexer.Num;
import inter.expr.Constant;
import java.util.ArrayList;

public class print extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant c = paras.get(0);
        System.out.print(c.toString());
        return Constant.False;
    }
}