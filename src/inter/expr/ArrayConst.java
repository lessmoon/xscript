package inter.expr;

import lexer.Word;
import symbols.Array;

public class ArrayConst extends Constant {
    public  final int size;
    private final Constant[] arr;

    public ArrayConst(Array t,int sz){
        super(Word.array,t);
        size = sz;
        arr = new Constant[size];
        Constant val = t.of.getInitialValue();
        for(int i = 0; i     < size;i++){
            arr[i] = val;
        }
    }

    @Override
    public Constant getValue(){
        return this;
    }

    public Constant setElement(int i,Constant c){
        arr[i] = c;
        return c;
    }

    public Constant getElement(int i){
        if(i >= size || i < 0){
            error("Index " + i + " out of range( 0~" + size + " )");
        }
        return arr[i];
    }

    public int getSize(){
        return size;
    }

    @Override
    boolean isChangeable(){
        /*array reference is not changeable*/
        return true;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        for(Constant c : arr){
            sb.append(" ").append(c);
        }
        sb.append(" ]");
        return sb.toString();
    }

}