package inter.expr;

import lexer.*;
import symbols.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class StructConst extends Constant {
    public  int size;
    final Constant[] table;
    
    public StructConst( Struct t ){
        super(Word.struct,t);
        table = new Constant[t.getVariableNumber()];
        
        /*Initial every member*/
        do{
            Iterator<Entry<Token,StructVariable>> iter = t.table.entrySet().iterator();
            while(iter.hasNext()){
                Entry<Token,StructVariable> info = iter.next();
                Type vt = info.getValue().type;
                int i = info.getValue().index;
                table[i] = vt.getInitialValue();
            }
            t = t.getFather();
        }while(t != null);
    }

    @Override
    public Constant getValue(){
        return this;
    }

    public Constant setElement(int index,Constant c){
        assert((table.length > index) && (index >= 0));
        table[index] = c;
        return c;
    }

    public Constant getElement(int index){
        assert((table.length > index) && (index >= 0));
        return table[index];
    }

    public int getSize(){
        return table.length;
    }
    
    public VirtualTable getVirtualTable(){
        return ((Struct)type).getVirtualTable();
    }

    @Override
    boolean isChangeable(){
        return true;
    }

    @Override
    public String toString(){
        return "Struct";
    }

}