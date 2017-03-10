package inter.expr;

import lexer.Word;
import symbols.Struct;
import symbols.VirtualTable;

public class StructConst extends Constant {
    public  int size;
    private final Constant[] memory;
    private Object extension = null;

    public StructConst( Struct t ){
        super(Word.struct,t);
        memory = t.createInstanceMemory();
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
        assert((memory.length > index) && (index >= 0));
        memory[index] = c;
        return c;
    }

    public Constant getElement(int index){
        assert((memory.length > index) && (index >= 0));
        return memory[index];
    }

    public int getSize(){
        return memory.length;
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