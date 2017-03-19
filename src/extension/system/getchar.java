package extension.system;

import extension.Function;
import inter.expr.Value;

import java.io.IOException;
import java.util.List;

public class getchar extends Function {
    public Value run(List<Value> args){
        try{
            return new Value(System.in.read());
        } catch(IOException e){
            return new Value(-2);
        }
    }
}