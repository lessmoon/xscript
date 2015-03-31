package lexer;

public class Str extends Token {
    public final String value;
    public Str(String v){
        super(Tag.STR);
        value = v;
    }

    public String toString(){
        return value;
    }

    public String toRawString() {
        StringBuffer sb = new StringBuffer();
        for(char c : value.toCharArray()){
            switch(c){
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
                sb.append(c);
                break;
            }
        }
        
        return "\"" + sb.toString() + "\"";
    }
}