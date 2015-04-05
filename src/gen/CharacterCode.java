package gen;

public class CharacterCode extends CodeBasic {
    final char value;
    
    public CharacterCode(char v){
        value = v;
    }
    
    public void writeCode(WriteBinaryCode wbc){
        wbc.write(value);
    }

    public int size(){
        return Character.SIZE;
    }
}