package extension.system;

import lexer.Num;
import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

public class open extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant c = paras.get(0);
        return new Constant(ExFile.openfile(c.toString()));
    }
}