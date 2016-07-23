package extension.ui;

import extension.Function;
import inter.expr.Constant;
import lexer.Num;
import lexer.Str;

import java.util.List;

/*
 * int addString(string str,int x,int y)
 * 
 */

public class setString extends Function {
    @Override
    public Constant run(List<Constant> paras){
        Constant id = paras.get(0);
        Constant s = paras.get(1);
		Constant x = paras.get(2);
        Constant y = paras.get(3);
        int iid = ((Num)(id.op)).value;
		String str =((Str)(s.op)).value;
		int ix = ((Num)(x.op)).value;
        int iy = ((Num)(y.op)).value;
		
        return new Constant(PaintPad.setString(iid,str,ix,iy));
    }

}