package extension;

import inter.expr.Value;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;
import symbols.Type;

import java.util.List;


public abstract class Function {
    public abstract Value run(List<Value> paras);
    /**
     * Invoked while before importing the model
     * Doing nothing by default.
     * @see runtime.LoadFunc#loadFunc(Type, String, String, Token, List, Dictionary, TypeTable)
     * @param dic the token dictionary
     * @param typeTable the table of types
     */
    public void init(Dictionary dic, TypeTable typeTable){
        //System.out.println("System:Default initialize function invoked");
    }
}