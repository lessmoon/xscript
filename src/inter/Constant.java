package inter;

import lexer.*;
import symbols.*;
import gen.*;

public class Constant extends Expr{
    public Constant(Token tok,Type p){
        super(tok,p);
    }

    public Constant(char c){
        super(new Char(c),Type.Str);
    }

    public Constant(int i){
        super(new Num(i),Type.Int);
    }

    public Constant(String s){
        super(new Str(s),Type.Str);
    }

    public Constant(float r){
        super(new Real(r),Type.Float);
    }

    public Constant getValue(){
        return this;
    }

    boolean isChangeable(){
        return false;
    }

    public static final Constant
        True = new Constant(Word.True,Type.Bool),
        False = new Constant(Word.False,Type.Bool);
    
    public String toString(){
        return op.toString();
    }

    public void emit(BinaryCodeGen bcg){
        if(type == Type.Int){
            bcg.emit(CodeTag.GET_ICONST);
            bcg.emit(((Num)op).value);
        } else if (type == Type.Bool){
            bcg.emit(CodeTag.GET_BCONST);
            if(this == True){
                bcg.emit(new IntegerCode(1));//true
            } else {// should be False
                bcg.emit(new IntegerCode(0));//false
            }
        } else if (type == Type.Float){
            bcg.emit(CodeTag.GET_RCONST);
            bcg.emit(new RealCode(((Real)op).value));//true
        } else if (type == Type.Char){
            bcg.emit(CodeTag.GET_CCONST);
            bcg.emit(new CharacterCode(((Char)op).value));
        } else if (type == Type.Str){
            bcg.emit(CodeTag.GET_SCONST);
            BinaryCodeGen.EmitMode m = bcg.getMode();
            bcg.setMode(m.CODE_CONST);
            int p = bcg.emit(new StringCode(((Str)op).value));
            bcg.setMode(m);
            bcg.emit(new IntegerCode(p));
        } else {//array or struct
            //error();
        }
    }
}