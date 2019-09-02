package inter.expr;

import lexer.Word;
import symbols.Struct;
import symbols.VirtualTable;

public class StructValue extends Value {
    public int size;
    private final Value[] memory;
    private Object extension = null;

    public StructValue(Struct t) {
        super(Word.struct, t);
        memory = t.createInstanceMemory();
    }

    public StructValue(Struct t, Object extension) {
        this(t);
        setExtension(extension);

    }

    public void setExtension(Object extension) {
        this.extension = extension;
    }

    public Object getExtension() {
        return extension;
    }

    @Override
    public Value getValue() {
        return this;
    }

    public Value setElement(int index, Value c) {
        assert ((memory.length > index) && (index >= 0));
        memory[index] = c;
        return c;
    }

    public Value getElement(int index) {
        assert ((memory.length > index) && (index >= 0));
        return memory[index];
    }

    public int getSize() {
        return memory.length;
    }

    public VirtualTable getVirtualTable() {
        return ((Struct) type).getVirtualTable();
    }

    @Override
    public boolean isChangeable() {
        return true;
    }

    @Override
    public String toString() {
        return "Struct";
    }

}