package extension;

import inter.expr.Value;
import runtime.Dictionary;
import runtime.TypeTable;

import java.util.List;


public abstract class Function {
    public abstract Value run(List<Value> paras);
    /*
     * Invoked while importing the model
     * Doing nothing by default.
     */
    public void init(Dictionary dic, TypeTable typeTable){
        //System.out.println("System:Default initialize function invoked");
    }
}