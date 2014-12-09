package lexer;

public class Word extends  Token {
    public String lexeme = "";
    public Word(String s, int tag){super(tag);lexeme = s;}
    public String toString(){
        return lexeme;
    }

    public static final Word
        array   =   new Word("[]",Tag.ARRAY),
        and     =   new Word("&&",Tag.AND),     or  = new Word("||",Tag.OR),
        eq      =   new Word("==",Tag.EQ),      ne  = new Word("!=",Tag.NE),
        le      =   new Word("<=",Tag.LE),      ge  = new Word(">=",Tag.GE),
        ass     =   new Word("=",'='),          not = new Word("!",'!'),
        ls      =   new Word("<",'<'),          gt  = new Word(">",'>'),
        add     =   new Word("+",'+'),          min = new Word("-",'-'),
        mult    =   new Word("*",'*'),          div = new Word("/",'/'),
        mod     =   new Word("%",'%'),          
        addass  =   new Word("+=",Tag.ADDASS),  minass  = new Word("-=",Tag.MINASS),
        multass =   new Word("*=",Tag.MULTASS), divass  = new Word("/=",Tag.DIVASS),
        modass  =   new Word("%=",Tag.MODASS),  inc     = new Word("++",Tag.INC),
        dec     =   new Word("--",Tag.DEC),     True    = new Word("true",Tag.TRUE),
        False   =   new Word("false",Tag.FALSE),print   = new Word("print",Tag.ID),
        strlen  =   new Word("strlen",Tag.ID);
}