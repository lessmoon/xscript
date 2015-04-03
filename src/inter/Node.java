package inter;

import lexer.*;
import gen.*;

public class Node {
    int     lexline;
    String  filename;
    Node() {
        lexline  = Lexer.line;
        filename = Lexer.filename;
    }

    void error(String s){
        throw new RuntimeException("Line " + lexline + " in file `" +  Lexer.filename + "':\n\t" + s);
    }
    
    public void emit(BinaryCodeGen bcg){}
}