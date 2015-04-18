package lexer;

public class Float extends Token {
    public final float value;
    public Float(float v){
        super(Tag.FLOAT);
        value = v;
    }

    public String toString() {
        return String.valueOf(value);
    }
}