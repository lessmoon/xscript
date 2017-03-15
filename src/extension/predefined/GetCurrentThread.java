package extension.predefined;

import extension.Function;
import inter.expr.StructValue;
import inter.expr.Value;
import runtime.Dictionary;
import runtime.LoadStruct;
import runtime.TypeTable;
import symbols.Struct;

import java.util.List;

/**
 * Created by lessmoon on 2016/9/12.
 */
public class GetCurrentThread extends Function {
    Struct threadType;

    @Override
    public Value run(List<Value> paras) {
        StructValue s = new StructValue(threadType);
        SimpleThread.SimpleThreadProxy t = new SimpleThread.SimpleThreadProxy();
        t.thread = Thread.currentThread();
        s.setExtension(t);
        return s;
    }

    @Override
    public void init(Dictionary dic, TypeTable typeTable){
        threadType = LoadStruct.getBoundStructOfClass(SimpleThread.class);
    }
}