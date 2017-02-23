package extension;

import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;


public abstract class Struct {
    /*
     * return the struct defined by the java code
     */
    public abstract symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable);
    
    
    public void init(Dictionary dic, TypeTable typeTable){
        //System.out.println("System:Default initialize function invoked");
    }
}