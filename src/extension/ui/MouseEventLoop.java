package extension.ui;

import extension.Function;
import inter.expr.StructValue;
import inter.expr.Value;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;

import java.util.List;

public class MouseEventLoop extends Function {
    public static Token fname = null;
    
    @Override
    public Value run(List<Value> paras){
        Value c = paras.get(0);
        if(c == Value.Null){
            return Value.False;
        }
        boolean x = PaintPad.SimpleMouseEvent((StructValue)c,fname);
        return x? Value.True: Value.False;
    }

    @Override
    public void init(Dictionary dic, TypeTable typeTable){
        fname = dic.getOrReserve("callback");
    }
}