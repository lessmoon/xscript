package inter;

import lexer.*;

public class Node {
    public final int     lexline;
    public final String  filename;
    Node() {
        lexline  = Lexer.line;
        filename = Lexer.filename;
    }

    void error(String s){
        throw new RuntimeException("Line " + lexline + " in file `" +  Lexer.filename + "':\n\t" + s);
    }
    /*
    void emitBinaryCode(BinaryCode x){
        //
    }
    */
}