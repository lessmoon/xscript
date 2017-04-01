package lexer;

public class Word extends  Token {
    public String lexeme = "";
    public Word(String s, int tag){super(tag);lexeme = s;}
    
    @Override
    public String toString(){
        return lexeme;
    }

    public static final Word
        Super   =   new Word("super",Tag.ID),
        This    =   new Word("this",Tag.ID),
        struct  =   new Word("{}",Tag.STRUCT),
        array   =   new Word("[]",Tag.ARRAY),
        brackets =   new Word("()",Tag.BRACKETS),
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
        False   =   new Word("false",Tag.FALSE),args    = new Word("_args_",Tag.ID),
        Null    =   new Word("null",Tag.NULL),Auto = new Word("auto",Tag.ID),
        EOF     =   new Word( "eof",-1);
}