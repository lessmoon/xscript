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

public class setString extends Function {
    @Override
    public Value run(List<Value> paras){
        Value id = paras.get(0);
        Value s = paras.get(1);
		Value x = paras.get(2);
        Value y = paras.get(3);
        int iid = ((Num)(id.op)).value;
		String str =((Str)(s.op)).value;
		int ix = ((Num)(x.op)).value;
        int iy = ((Num)(y.op)).value;
		
        return new Value(PaintPad.setString(iid,str,ix,iy));
    }

}