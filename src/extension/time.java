package extension;

import inter.Constant;

import java.util.ArrayList;
import java.util.Date;

public class time extends Function {
    public Constant run(ArrayList<Constant> paras){
        int i = (int)(new Date().getTime()/1000);
        return new Constant( i ) ;
    }
}