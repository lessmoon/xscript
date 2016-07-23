package extension.ui;

import lexer.Num;
import lexer.Str;
import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

/*
 * int addString(string str,int x,int y)
 * 
 */

public class setString extends Function {
    public Constant run(ArrayList<Constant> paras){
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