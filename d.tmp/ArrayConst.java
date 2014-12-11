package inter;

import lexer.*;
import symbols.*;

import java.util.ArrayList;

public class ArrayConst extends Constant {
    public  int size;
    ArrayList<Constant> arr;
    
    public ArrayConst( Array t){
        super(Word.array,t);
        size = t.getSize();
        arr = new ArrayList<Constant>( size );
    }

    public Constant getValue(){
        return this;
    }

    public Constant setElement(int i,Constant c){
        arr.set(i,c);
        return c;
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
            sb.append(" ");
            sb.append(c.toString());
        }
        sb.append(" ]");
        return sb.toString();
    }

}