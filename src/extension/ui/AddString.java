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

public class AddString extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant s = paras.get(0);
		Constant x = paras.get(1);
        Constant y = paras.get(2);
		String str =((Str)(s.op)).value;
		int ix = ((Num)(x.op)).value;
        int iy = ((Num)(y.op)).value;
		
        return new Constant(PaintPad.addString(str,ix,iy));
    }
}