package inter;

import lexer.*;
import symbols.*;

class IntSet extends Set {
    
    public IntSet(Token tok,Expr i,Expr x){
        super(tok,i,x);
    }

    public Constant getValue(){
        int r = ((Num)(expr.getValue().op)).value;
        int l = ((Num)(id.getValue().op)).value;
        
        switch(op.tag){
        case Tag.ADDASS:
            return id.setValue( new Constant(l + r));
        case Tag.MINASS:
            return id.setValue( new Constant(l - r));
        case Tag.MULTASS:
            return id.setValue( new Constant(l * r));
        case Tag.DIVASS:
            return id.setValue( new Constant(l / r));
        case Tag.MODASS:
            return id.setValue( new Constant(l % r));
        default:
            return null;
        }
    }
}

class CharSet extends Set {
    
    public CharSet(Token tok,Expr i,Expr x){
        super(tok,i,x);
    }

    public Constant getValue(){
        char r = ((Char)(expr.getValue().op)).value;
        char l = ((Char)(id.getValue().op)).value;
        
        switch(op.tag){
        case Tag.ADDASS:
            return id.setValue( new Constant(l + r));
        case Tag.MINASS:
            return id.setValue( new Constant(l - r));
        case Tag.MULTASS:
            return id.setValue( new Constant(l * r));
        case Tag.DIVASS:
            return id.setValue( new Constant(l / r));
        case Tag.MODASS:
            return id.setValue( new Constant(l % r));
        default:
            return null;
        }
    }
}

class RealSet extends Set {
    public RealSet(Token tok,Expr i,Expr x){
        super(tok,i,x);
    }
    
    public Constant getValue(){
        float r = ((Real)(expr.getValue().op)).value;
        float l = ((Real)(id.getValue().op)).value;
        
        switch(op.tag){
        case Tag.ADDASS:
            return id.setValue( new Constant(l + r));
        case Tag.MINASS:
            return id.setValue( new Constant(l - r));
        case Tag.MULTASS:
            return id.setValue( new Constant(l * r));
        case Tag.DIVASS:
            return id.setValue( new Constant(l / r));
        default:
            return null;
        }
    }
}

class StrSet extends Set {
    public StrSet(Token tok,Expr i,Expr x){
        super(tok,i,x);
    }

    public Constant getValue(){
        String r = ((Str)(expr.getValue().op)).value;
        String l = ((Str)(id.getValue().op)).value;
        
        switch(op.tag){
        case Tag.ADDASS:
            return id.setValue( new Constant(l + r));
        default:
            return null;
        }
    }
}


public class SetFactory {
    static public Set getSet(Token tok,Expr i,Expr x){
        if(tok.tag == '='){
            return new Set(tok,i,x);
        } else if( i.type == Type.Int ){
            return new IntSet(tok,i,x);
        } else if( i.type == Type.Float){
            return new RealSet(tok,i,x);
        } else if( i.type == Type.Str){
            return new StrSet(tok,i,x);
        } else {
            return new CharSet(tok,i,x);
        }
    }
}