package inter.expr;

import lexer.Token;
import lexer.Word;
import symbols.Struct;
import symbols.StructVariable;

public class StructMemberAccess extends Var {
    protected Expr            value;
    private final   Token           member;
    private final   int             index;
    
    public StructMemberAccess(Expr v,Token m){
        super(Word.struct,null);
        value = v;
        member = m;
        index = check();
    }

    int check(){
        if( ! (value.type instanceof Struct) ){
            error("struct access can't be used for " + value.type );
        }
        StructVariable variable = ((Struct)(value.type)).getVariable(member);
        if( variable == null ){
            error("can't find member `" + member + "' in " + value.type);
        }
        type = variable.type;
        return variable.index;
    }

    @Override
    public boolean isChangeable(){
        return true;
    }
    
    @Override
    public Expr optimize(){
        value = value.optimize();
        return this;
    }

    @Override
    public Value getValue(){
        Value c = value.getValue();
        if(c == Value.Null){
            error("null pointer error:try to get member `" + member + "' of a null struct");
        }
        StructValue s = (StructValue)c;
        Value tmp = s.getElement(index);
        return tmp;
    }

    @Override
    public Value setValue(Value v){
        Value c = value.getValue();
        if(c == Value.Null){
            error("null pointer error:try to set member `" + member + "' of a null struct ");
        }
        StructValue s = (StructValue)c;
        return s.setElement(index,v);
    }
    
    public String toString(){
        return value.toString() + ".[" + index + "]" + member;
    }
}