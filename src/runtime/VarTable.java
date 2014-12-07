package runtime;

import inter.*;
import lexer.*;
import symbols.*;

import java.util.*;

class ConstantReference {
    Constant v; 
    ConstantReference(Constant v){
        this.v = v;
    }
}

public class VarTable {
    static private VarTable t = new VarTable(null);

    HashMap <Token,ConstantReference> table = new HashMap <Token,ConstantReference>();
    public final VarTable prev ;

    private VarTable(VarTable prev){
        this.prev = prev;
    }

    static public VarTable getTop(){
        return t;
    }

    static public VarTable popTop(){
        VarTable tmp = t;
        //System.out.println("Want undo old stack top " );
        if(t.prev == null)
            return null;
        else{
            //System.out.println("Undo old stack top " );
            t = t.prev;
            return tmp;
        }
    }

    static public VarTable pushTop(){
        //System.out.println("New stack top " );
        t = new VarTable(t);
        return t;
    }

    public boolean pushVar(Token id,Constant v){
        
        if(table.containsKey(id))
            return false;
        table.put(id,new ConstantReference(v));
        return true;
    }

    public boolean topContains(Token id){
        return table.containsKey(id);
    }

    public boolean contains(Token id){
        VarTable tb = this;
        while(tb != null){
            if(tb.table.containsKey(id)){
                return true;
            }
            tb = tb.prev;
        }
        return false;
    }

    public Constant getVar(Token id){
        ConstantReference ref = null;
        VarTable tb = this;
        while(tb != null){
            ref = tb.table.get(id);
            if(ref != null)
                return ref.v;
            tb = tb.prev;
        }
        return null;
    }

    public Constant setVar(Token id,Constant v){
        VarTable tb = this;
        ConstantReference ref;
        while(tb != null){
            if((ref = tb.table.get(id)) != null){
                
                return (ref.v = v);
            }
            tb = tb.prev;
        }
        return null;
    }
}