package inter.code;

public class IntReference {
    int value = 0;
    public IntReference(){
    }

    public IntReference(int v){
        value = v;
    }

    public void setValue(int v){
        this.value = v;
    }

    public int getValue(){
        return value;
    }
}