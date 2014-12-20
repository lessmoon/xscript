package symbols;

import lexer.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Struct extends Type {
    public HashMap<Token,Type> table = new HashMap<Token,Type>();

    public Struct(Token name){
        super(name.toString(),Tag.BASIC);
        //assert(!t.empty());
    }

    public boolean equals(Type t){
        return t == this;
    }

    public Type addEntry(Token mname,Type t){
        return table.put(mname,t);
    }
    
    /*
     * return the type of the member named mname
     * return null if it doesn't exist
     */
    public Type getType(Token mname){
        return table.get(mname);
    }

    public String toString(){
        return  "struct " + super.toString();
    }

}