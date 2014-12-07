package lexer;

public class Char extends Token{
    public final char value;
    public Char(char v){
        super(Tag.CHAR);
        value = v;
    }

    public String toString(){
        return String.valueOf(value);
    }
}