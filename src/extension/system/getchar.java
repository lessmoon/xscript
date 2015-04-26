package extension.system;

import inter.expr.Constant;
import java.util.ArrayList;
import extension.Function;

import java.io.IOException;

public class getchar extends Function {
    public Constant run(ArrayList<Constant> paras){
        try{
            Constant c = new Constant(System.in.read());
            return c;
        } catch(IOException e){
            return new Constant(-2);
        }
    }
}