package extension.ui;

import lexer.Token;
import inter.expr.Constant;
import inter.expr.StructConst;
import extension.Function;
import runtime.Dictionary;
import runtime.TypeTable;

import java.util.List;

public class MouseEventLoop extends Function {
    public static Token fname = null;
    
    @Override
    public Constant run(List<Constant> paras){
        Constant c = paras.get(0);
        if(c == Constant.Null){
            return Constant.False;
        }
        boolean x = PaintPad.SimpleMouseEvent((StructConst)c,fname);
        return x?Constant.True:Constant.False;
    }

    @Override
    public void init(Dictionary dic, TypeTable typeTable){
        fname = dic.getOrReserve("callback");
    }
}