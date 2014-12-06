package inter;

import lexer.*;import symbols.*;

public class Constant extends Expr{
    public Constant(Token tok,Type p){
        super(tok,p);
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
}