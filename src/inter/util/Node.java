package inter.util;

import lexer.*;

public class Node {
    public final int     lexline;
    public final String  filename;
    public static boolean enableWarning = false;
    public static void setEnableWarning(boolean t){
        enableWarning = t;
    }
    
    public Node() {
        lexline  = Lexer.line;
        filename = Lexer.filename;
    }

    public void error(String s){
        throw new RuntimeException("line " + lexline + " in file `" +  filename + "':\n\t" + s);
    }
    
    public void warning(String s){
        if(enableWarning){
            System.err.println("line " + lexline + " in file `" +  filename + "':\n\t" + s);
        }
    }
}