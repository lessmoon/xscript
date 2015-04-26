package extension.system;

import lexer.Num;
import inter.expr.Constant;
import extension.Function;

import java.util.ArrayList;

public class sleep extends Function {
    public Constant run(ArrayList<Constant> paras){
        Constant duration = paras.get(0);    
        int d = ((Num)(duration.op)).value;
        try {
            Thread.sleep(d);
        } catch (InterruptedException e) {
            e.printStackTrace(); 
        }
        return null;
    }
}