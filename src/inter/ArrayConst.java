package inter;

import lexer.*;
import symbols.*;

import java.util.ArrayList;

public class ArrayConst extends Constant {
    public  final int size;
    final Constant[] arr;

    public ArrayConst( Array t ){
        super(Word.array,t);
        size = t.getElementNumber();
        arr = new Constant[size];
        if(t.of instanceof Array){
            for(int i = 0 ; i < size ;i++){
                arr[i] = new ArrayConst((Array)t.of);
            }
        }
    }

    public Constant getValue(){
        return this;
    }

    public Constant setElement(int i,Constant c){
        arr[i] = c;
        return c;
    }

    public Constant getElement(int i){
        return arr[i];
    }
    
    public int getSize(){
        return size;
    }

    boolean isChangeable(){
        return true;
    }

    public String toString(){
        StringBuffer sb = new StringBuffer("[");
        for(Constant c : arr){
            sb.append(" " + c);
        }
        sb.append(" ]");
        return sb.toString();
    }

}