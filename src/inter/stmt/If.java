package inter.stmt;

import inter.expr.Expr;
import inter.expr.Value;
import symbols.Type;

public class If extends Stmt{
    Expr expr;
    Stmt stmt;
    public If(Expr x,Stmt s){
        expr = x;
        stmt = s;
        if(expr.type != Type.Bool)
            expr.error("boolean required in if");
    }

    @Override
    public void run(){
        if(expr.getValue() != Value.False){
            stmt.run();
        }
    }

    @Override
    public Stmt optimize(){
        stmt = stmt.optimize();
        if(expr == Value.False){/*constant False,it will never happen to run the stmt*/
            return Stmt.Null;
        } else if(expr == Value.True){
            return stmt;
        }
        return this;
    }
    
    @Override
    public String toString(){
        return "if(" + expr + "){\n"
                + stmt
                + "}\n";
    }
    
    /*
        void emitBinaryCode(BinaryCode x){
            expr.emitBinaryCode(x);
            x.emit(J_OFF);
            int x = x.getCurrentAddress();
            x.emitIntegerOffsetReference(after,x);
            stmt.emitBinaryCode(x);
            int after = x.getCurrentAddress();
         }
    */ 
}