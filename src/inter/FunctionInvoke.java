package inter;

import lexer.*;
import symbols.*;

public class FunctionInvoke extends Expr {
    Expr para;
    public FunctionInvoke(Token tok,Type t,Expr p){
        super(tok,t);
        para = p;
    }

    boolean isChangeable(){
        return true;
    }

    public String toString(){
        return op.toString();
    }
    
    public Constant getValue(){
        System.out.print(para.getValue());
        return Constant.False;/*At Present*/
    }
}