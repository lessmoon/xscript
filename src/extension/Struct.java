package extension;

import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;


public abstract class Struct {
    /**
     * setup a struct
     * native<class-path>{
     *     [struct-name]: struct struct-name;
     *     [struct-name]: struct struct-name{
     *         functions and variable;
     *     }
     * }
     * @param sname the struct name it is the class name by default
     * @param dic the token dictionary
     * @param typeTable the type table
     * @return the struct defined by extension java code
     * @see #setup(symbols.Struct, Dictionary, TypeTable)
     */
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return setup(new symbols.Struct(sname),dic,typeTable);
    }


    /**
     * setup a struct using a declared struct {@code struct}
     * @param struct struct to fill
     * @param dic the token dictionary
     * @param typeTable the type table
     * @return the struct defined by the java code
     */
    public abstract symbols.Struct setup(symbols.Struct struct, Dictionary dic, TypeTable typeTable);

    public void init(Dictionary dic, TypeTable typeTable){
        //System.out.println("System:Default initialize function invoked");
    }
}