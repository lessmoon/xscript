package extension.ui;

import extension.Function;
import inter.expr.Value;
import lexer.Num;
import lexer.Str;

import java.util.List;

/*
 * int addString(string str,int x,int y)
 * 
 */

public class AddString extends Function {
    public Value run(List<Value> paras){
        Value s = paras.get(0);
		Value x = paras.get(1);
        Value y = paras.get(2);
		String str =((Str)(s.op)).value;
		int ix = ((Num)(x.op)).value;
        int iy = ((Num)(y.op)).value;
		
        return new Value(PaintPad.addString(str,ix,iy));
    }
}