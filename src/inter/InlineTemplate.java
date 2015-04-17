package inter;

import lexer.*;
import symbols.*;

import java.util.ArrayList;

public class InlineTemplate extends Node {
    Expr expr;
    ArrayList<ExprReference> paralist;

    public InlineTemplate(Expr e,ArrayList<ExprReference> pl){
        expr = e;
        paralist = pl;
    }
    
    public Expr reduce(){
        
    }
}