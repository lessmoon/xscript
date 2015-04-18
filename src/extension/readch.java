package extension;

import lexer.Num;
import inter.expr.Constant;
import java.util.ArrayList;

public class readch extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant c = paras.get(0);
        int fid = ((Num)(c.op)).value;
        return new Constant(ExFile.readch(fid));
    }
}