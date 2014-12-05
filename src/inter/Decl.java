package inter;

import lexer.*;
import symbols.*;
import runtime.*;

public class Decl extends Stmt {
    Token id;
    Type  type;
    
    public Decl(Token i,Type t){
        id = i;
        type = t;
    }
    
    public void run(){
        /**/
        VarTable.getTop().pushVar(id,null);
    }
}