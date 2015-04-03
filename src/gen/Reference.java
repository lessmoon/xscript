package gen;

public class Reference<T>{
    T value ;
    public Reference(){
        value = null;
    }
    
    public Reference(T v){
        value = v;
    }

    public void setValue(T v){
        value = v;
    }

    public T getValue(){
        return value;
    }
    
}