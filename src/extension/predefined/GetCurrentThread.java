package extension.predefined;

import extension.Function;
import inter.expr.Constant;
import inter.expr.StructConst;
import runtime.Dictionary;
import runtime.TypeTable;
import symbols.Struct;

import java.util.List;

/**
 * Created by lessmoon on 2016/9/12.
 */
public class GetCurrentThread extends Function {
    Struct threadType;

    @Override
    public Constant run(List<Constant> paras) {
        StructConst s = new StructConst(threadType);
        SimpleThread.SimpleThreadProxy t = new SimpleThread.SimpleThreadProxy();
        t.thread = Thread.currentThread();
        s.setExtension(t);
        return s;
    }

    @Override
    public void init(Dictionary dic, TypeTable typeTable){
        threadType = (Struct)typeTable.getType(dic.getOrReserve("Thread"));
    }
}