package inter.util;

import lexer.Lexer;

public class Node {
    public final int line;
    public final String  filename;
    public final int offset;
    private static boolean enableWarning = false;
    public static void setEnableWarning(boolean t){
        enableWarning = t;
    }
    
    public Node() {
        line = Lexer.line;
        filename = Lexer.filename;
        offset = Lexer.offset;
    }

    public void error(String s){
        throw new RuntimeException("in file `" +  filename + " at " + line + ":" + offset +"':\n\t" + s);
    }
    
    public void warning(String s){
        if(enableWarning){
            System.err.println( "in file `" +  filename + " at " + line + ":" + offset + "':\n\t" + s);
        }
    }
}