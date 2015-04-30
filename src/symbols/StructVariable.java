package symbols;

import lexer.*;
import inter.stmt.FunctionBasic;
import inter.stmt.MemberFunction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class StructVariable{
    public final Type type;
    public final int  index;
    
    public StructVariable(Type t,int i){
        type = t;
        index =i;
    }
}