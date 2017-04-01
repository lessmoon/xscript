package inter.expr;

import lexer.Token;
import runtime.VarTable;
import symbols.EnvEntry;
import symbols.Type;

public class StackVar extends Var {
    final int stackLevel;
    final int stackOffset;
    public StackVar(Token w,Type t,int sl,int o){
        super(w,t);
        stackLevel = sl;
        stackOffset = o;
    }

    @Override
    public boolean isChangeable(){
        return true;
    }

    @Override
    public Value getValue(){
        return VarTable.getVar(stackLevel, stackOffset);
    }
    
    public Value setValue(Value v){
        return  VarTable.setVar(stackLevel, stackOffset,v);
    }

    @Override
    public String toString(){
        return "$$" + op + "["+ stackLevel +","+ stackOffset +"]";
    }

    @Override
    public boolean equals(Object o){
        return this == o ||
                o instanceof StackVar && ((StackVar) o).offset == offset && ((StackVar) o).stackLevel == stackLevel && ((StackVar) o).type.isCongruentWith(this.type);
    }

    @Override
    public int hashCode(){
        //NOTE:Type should join the hash code computing,because it is not necessary to be same hashCode between two same types
        return this.stackLevel << 7 | this.offset;
    }

    public static StackVar stackVar(Token id, EnvEntry ee,int toplevel){
        return stackVar(id,ee.type,ee.stacklevel,ee.offset,toplevel);
    }

    public static StackVar stackVar(Token id,Type type,int stackLevel,int stackOffset,int toplevel){
        return stackLevel == 0 ? new AbsoluteVar(id, type, 0, stackOffset) : new StackVar(id, type, toplevel - stackLevel, stackOffset);
    }
}