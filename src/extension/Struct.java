package extension;

import lexer.Token;
import runtime.Dictionary;


public abstract class Struct {
    /*
     * return the struct defined by the java code
     */
    public abstract symbols.Struct setup(Token sname,Dictionary dic);
    
    
    public void init(Dictionary dic){
        //System.out.println("System:Default initialize function invoked");
    }
}