package extension;

import lexer.Num;
import lexer.Char;
import inter.Constant;
import java.util.ArrayList;

public class writech extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant c = paras.get(0);
        int fid = ((Num)(c.op)).value;
        c = paras.get(1);
        char ch = ((Char)(c.op)).value;
        return new Constant(ExFile.writech(fid,ch));
    }
}