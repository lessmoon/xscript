package extension.ui;

import lexer.Token;
import lexer.Num;
import inter.expr.Constant;
import inter.expr.StructConst;
import extension.Function;
import runtime.Dictionary;

import java.util.ArrayList;

public class EventLoop extends Function {
    public static Token fname = null;
    
    @Override
    public Constant run(ArrayList<Constant> paras){
        Constant c = paras.get(0);
        if(c == Constant.Null){
            return Constant.False;
        }
        boolean x = PaintPad.KeyBoardEvent((StructConst)c,fname);
        return x?Constant.True:Constant.False;
    }

    @Override
    public void init(Dictionary dic){
        fname = dic.getOrreserve("callback");
    }
}