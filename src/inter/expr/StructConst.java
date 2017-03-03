package inter.expr;

import lexer.Token;
import lexer.Word;
import symbols.Struct;
import symbols.StructVariable;
import symbols.Type;
import symbols.VirtualTable;

import java.util.Map.Entry;

public class StructConst extends Constant {
    public  int size;
    private final Constant[] table;
    private Object extension = null;

    public StructConst( Struct t ){
        super(Word.struct,t);
        table = new Constant[t.getVariableNumber()];
        
        /*Initial every member*/
        do{
            for (Entry<Token, StructVariable> info : t.table.entrySet()) {
                Type vt = info.getValue().type;
                int i = info.getValue().index;
                table[i] = vt.getInitialValue();
            }
            t = t.getFather();
        }while(t != null);
    }

    public StructConst( Struct t ,Object extension ){
        this(t);
        setExtension(extension);
        
    }
    
    public void setExtension(Object extension){
        this.extension = extension;
    }

    public Object getExtension(){
       return extension;
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
    public boolean isChangeable(){
        return true;
    }

    @Override
    public String toString(){
        return "Struct";
    }

}