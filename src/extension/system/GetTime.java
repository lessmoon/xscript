package extension.system;

import lexer.Token;
import lexer.Num;
import inter.expr.Constant;
import inter.expr.StructConst;
import extension.Function;
import runtime.Dictionary;

import java.util.ArrayList;
import java.util.Calendar;


public class GetTime extends Function {
    public static Token fname = null;
    
    @Override
    public Constant run(ArrayList<Constant> paras){
        Constant c = paras.get(0);
        if(c == Constant.Null){
            return Constant.Null;
        }
		Calendar x = Calendar.getInstance();
		((StructConst)c).setElement(0,new Constant(x.get(Calendar.HOUR)));
		((StructConst)c).setElement(1,new Constant(x.get(Calendar.MINUTE)));
		((StructConst)c).setElement(2,new Constant(x.get(Calendar.SECOND)));
        return Constant.Null;
    }

}