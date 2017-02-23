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
        StructVariable variable = ((Struct)(value.type)).getMemberVariableType(member);
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
    public Constant getValue(){
        Constant c = value.getValue();
        if(c == Constant.Null){
            error("null pointer error:try to get member `" + member + "' of a null struct");
        }
        StructConst s = (StructConst)c;
        return s.getElement(index);
    }

    @Override
    public Constant setValue(Constant v){
        Constant c = value.getValue();
        if(c == Constant.Null){
            error("null pointer error:try to set member `" + member + "' of a null struct ");
        }
        StructConst s = (StructConst)c;
        return s.setElement(index,v);
    }
    
    public String toString(){
        return value.toString() + ".[" + index + "]" + member;
    }
}