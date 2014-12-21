package lexer;


import symbols.*;

import java.util.HashMap;
import java.util.Stack;
import java.io.*; 

class Info {
    InputStreamReader in;
    int         peek;
    String      filename;
    int         line;
    String      path;

    public Info(InputStreamReader i,int p,String f,int l,String pt){
        in = i;
        peek = p;
        filename = f;
        line = l;
        path = pt;
    }
}

public class Lexer {
    public static int line = 1;
    public static String filename = "";
    int peek = ' ';
    Stack<Info> list = new Stack<Info>();
    InputStreamReader in = null;
    HashMap<String,Token> words = new HashMap<String,Token>();

    void reserve(Word w) {
        words.put(w.lexeme,w);
    }

    public void error(String s){
        throw new RuntimeException("Line " + line + " in file `" +  filename + "':\n\t" + s);
    }
    
    public Token defType(Type t) {
        return words.put(t.lexeme,t);
    }

    public Lexer() {
        reserve( new Word("if",Tag.IF) );
        reserve( new Word("else",Tag.ELSE) );
        reserve( new Word("while",Tag.WHILE) );
        reserve( new Word("for",Tag.FOR) );
        reserve( new Word("do",Tag.DO) );
        reserve( new Word("break",Tag.BREAK) );
        reserve( new Word("def",Tag.DEF) );
        reserve( new Word("return",Tag.RETURN) );
        reserve( new Word("loadfunc",Tag.LDFUNC));
        reserve( new Word("import",Tag.IMPORT));
        reserve( new Word("struct",Tag.STRUCT));
        
        
        reserve( Word.True );
        reserve( Word.False );
        reserve( Type.Int );
        reserve( Type.Char );
        reserve( Type.Bool );
        reserve( Type.Float );
        reserve( Type.Str );
    }

    void save(String path){
        list.push(new Info(in,peek,filename,line,path));
    }

    /*
     * Save the file name
     */
    public void open( String file ) throws IOException {
        /*for the first open*/
        File f = new File(file);
        f = f.getCanonicalFile();
        if(in != null)
            save(System.getProperty("user.dir"));
        try{
            System.setProperty("user.dir",f.getCanonicalFile().getParent());
        } catch(Exception e){
            error(e.toString());
        }
        if(!f.isFile()){
            error("File `" + file + "' doesn't exist");
        }

        if(!f.canRead()) {
            error("File `" + file + "' can't be read");
        }

        in = new InputStreamReader(new FileInputStream(f));
        line = 1;
        peek = ' ';
        filename = file;
    }

    /*
     * Recover the lexer's info,if the stack is 
     * empty,do nothing
     * Return false if the stack is empty,or true
     */
    public boolean recover(){
        if(list.empty()){
            return false;
        } else {
            Info i = list.pop();
            in = i.in;
            filename = i.filename;
            line = i.line;
            peek = i.peek;
            try{
                System.setProperty("user.dir",i.path);
            } catch(Exception e){
                error(e.toString());
            }
            return true;
        }
    }

    void readch() throws IOException {
        int p = in.read();
        while(p < 0){
            in.close();
            if(!recover()){
                break;
            }
            p = in.read();
        }
        peek = p > 0?(char) p : p;
    }

    boolean readch(char c) throws IOException {
        readch();
        if(peek != c)
            return false;
        peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        for(;;readch()){
            if(peek == '\n') {
                line++;
            } else if(Character.isWhitespace(peek)){
                continue;
            } else if(peek == '/'){
                if(readch('*')){
                    readch();
                    do{
                        while(peek != '*'){
                            if(peek == '\n')
                                line ++;
                            readch();
                        }
                        readch();
                    }while( peek != '/' );
                } else if( peek == '/' ){
                    while(!readch('\n'));
                    line++;
                } else {
                    return new Token('/');
                }
            } else {
                break;
            }
        }

        switch(peek){
            case '&':
                if(readch('&')) 
                    return Word.and;
                else 
                    return new Token('&');
            case '|':
                if(readch('|')) 
                    return Word.or;
                else 
                    return new Token('|');
            case '=':
                if(readch('='))
                    return Word.eq;
                else
                    return Word.ass;
            case '!':
                if(readch('='))
                    return Word.ne;
                else
                    return Word.not;
            case '<':
                if(readch('='))
                    return Word.le;
                else
                    return Word.ls;
            case '>':
                if(readch('='))
                    return Word.ge;
                else 
                    return Word.gt;
            case '+':
                if(readch('+'))
                    return Word.inc;
                else if(peek == '='){
                    peek = ' ';
                    return Word.addass;
                } else
                    return Word.add;
            case '-':
                if(readch('-'))
                    return Word.dec;
                else if(peek == '='){
                    peek = ' ';
                    return Word.minass;
                }else
                    return Word.min;
            case '*':
                if(readch('='))
                    return Word.multass;
                else
                    return Word.mult;
            case '/':
                if(readch('='))
                    return Word.divass;
                else
                    return Word.div;
            case '%':
                if(readch('='))
                    return Word.modass;
                else 
                    return Word.mod;
        }

        if(Character.isDigit(peek)){
            int v = 0;
            do{
                v = 10 * v + Character.digit(peek,10);
                readch();
            }while(Character.isDigit(peek));
            if(peek != '.')
                return new Num(v);
            float x = v;
            float d = 10;
            for(;;){
                readch();
                if(! Character.isDigit(peek))
                    break;
                x = x + Character.digit(peek,10)/d;
                d = d*10;
            }
            return new Real(x);
        }

        if(Character.isLetter(peek)||peek == '_'){
            StringBuffer b = new StringBuffer();
            do{
                b.append((char)peek);
                readch();
            } while(Character.isLetterOrDigit(peek)||peek == '_');
            String s = b.toString();
            Word w = (Word)words.get(s);
            if(w != null)
                return w;
            w = new Word(s,Tag.ID);
            words.put(s,w);
            return w;
        }

        if(peek == '\"'){
            StringBuffer b = new StringBuffer();
            readch();
            while(peek != '\"'){
                int c = peek;
                if(peek == '\\'){
                    readch();
                    c = peek;
                    switch(peek){
                    case '\'':
                    case '\"':
                    case '?':
                    case '\\':
                        break;
                    case 'b':
                        c = '\b';
                        break;
                    case 'f':
                        c = '\f';
                        break;
                    case 'n':
                        c = '\n';
                        break;
                    case 'r':
                        c = '\r';
                        break;
                    case 't':
                        c = '\t';
                        break;
                    default:
                        /*error*/
                    }
                }
                b.append((char)c);
                readch();
            }
            readch();
            return new Str(b.toString());
        } else if( peek == '\''){
            readch();
            int c = peek;

            if(peek == '\\'){
                readch();
                c = peek;
                switch(peek){
                case '\'':
                case '\"':
                case '?':
                case '\\':
                    break;
                case 'b':
                    c = '\b';
                    break;
                case 'f':
                    c = '\f';
                    break;
                case 'n':
                    c = '\n';
                    break;
                case 'r':
                    c = '\r';
                    break;
                case 't':
                    c = '\t';
                    break;
                default:
                    /*error*/
                }
                
            }
            if(!readch('\''))
                error("Wrong character constant.");
            return new Char((char)c);
        }

        Token tok = new Token(peek);
        peek = ' ';
        return tok;
    }

    static public void main(String[] args) throws IOException {
        Token t = null;
        Lexer lex = new Lexer();
        do{
            t = lex.scan();
            System.out.println(t.toString());
        }while(t.tag != -1);
    }
}