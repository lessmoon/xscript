package extension.system;

import extension.Function;
import inter.expr.Constant;
import inter.expr.StructConst;
import lexer.Token;

import java.util.Calendar;
import java.util.List;


public class GetTime extends Function {
    public static Token fname = null;
    
    @Override
    public Constant run(List<Constant> paras){
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