package extension;

import inter.expr.Constant;
import runtime.Dictionary;

import java.util.ArrayList;


public abstract class Function {
    public abstract Constant run(ArrayList<Constant> paras);
    /*
     * Invoked while importing the model
     * Doing nothing by default.
     */
    public void init(Dictionary dic){
        //System.out.println("System:Default initialize function invoked");
    }
}