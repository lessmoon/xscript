package gen;

public class StringCode extends CodeBasic {
    final String ref;
    
    public StringCode(String s){
        ref = s;
    }
    
    public void writeCode(WriteBinaryCode wbc){
        wbc.write(ref.length());
        for(int i = 0 ;i < ref.length();i++)
        wbc.write(ref.charAt(i));
    }

    public int size(){
        return Integer.SIZE/8 + Character.SIZE / 8 * ref.length();
    }
}