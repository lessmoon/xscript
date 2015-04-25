package inter.stmt;

import inter.code.*;

import java.util.ArrayList;

public class Seq extends Stmt{
    Stmt stmt1;
    Stmt stmt2;

    public Seq(Stmt s1,Stmt s2){
        stmt1 = s1;
        stmt2 = s2;
    }
    
    @Override
    public void run(){
        if(stmt1 != Stmt.Null){
            stmt1.run();
        }
        if(stmt2 != Stmt.Null){
            stmt2.run();
        }
    }
    
    @Override
    public Stmt optimize(){
        stmt1 = stmt1.optimize();
        if(stmt1 == Stmt.Null)
            return stmt2.optimize();
        if(stmt1 instanceof Return 
            || stmt1 instanceof Break
            || stmt1 instanceof Continue)/*anyway,stmt2 will *NOT* run absolutely*/
            return stmt1;
        stmt2 = stmt2.optimize();
        if(stmt2 == Stmt.Null)
            return stmt1;
        return this;
    }

    @Override
    public void emitCode(ArrayList<SerialCode> i){
        stmt1.emitCode(i);
        stmt2.emitCode(i);
    }

    @Override
    public String toString(){
        return stmt1.toString() + stmt2.toString();
    }
}