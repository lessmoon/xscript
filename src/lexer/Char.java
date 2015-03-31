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
    
    public String toRawString(){
        StringBuffer sb = new StringBuffer();
       
        switch(value){
        case '\b':
            sb.append("\\b");
            break;
        case '\f':
            sb.append("\\f");
            break;
        case '\n':
            sb.append("\\n");
            break;
        case '\r':
            sb.append("\\r");
            break;
        case '\t':
            sb.append("\\t");
            break;
        case '\'':
        case '\"':
        case '?':
        case '\\':
            sb.append("\\");
        default:
            sb.append(value);
            break;
        }

        return "\'" + sb.toString() + "\'";
    }
}