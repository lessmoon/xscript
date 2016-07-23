package extension.system;

import inter.expr.Constant;
import extension.Function;

import java.io.IOException;
import java.util.List;

public class getchar extends Function {
    public Constant run(List<Constant> paras){
        try{
            return new Constant(System.in.read());
        } catch(IOException e){
            return new Constant(-2);
        }
    }
}