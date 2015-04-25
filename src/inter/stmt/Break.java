package inter.stmt;

import runtime.*;
import inter.code.*;

import java.util.ArrayList;

public class Break extends Stmt implements SerialCode{
    static public  final Throwable BreakCause = new Throwable();
    Stmt stmt;
    public final int sizeOfStack;
    public Break(int s){
        if( Stmt.BreakEnclosing == Stmt.Null )
            error("unenclosed break");
        stmt = Stmt.BreakEnclosing;
        sizeOfStack = s;
    }

    @Override
    public void run(){
        /*
         * I *KNOW* it is wrong use of exception
         * But it works well.
         * Maybe I will change the virtual machine.
         */
        for(int i = 0 ; i < sizeOfStack;i++)
            VarTable.popTop();

        throw new RuntimeException(BreakCause);
    }
    
    @Override
    public void serially_run(RunEnv r){
        for(int i = 0 ; i < sizeOfStack;i++)
            VarTable.popTop();
        r.jumpTo(stmt.getTailAddr());
    }

    @Override
    public void emitCode(ArrayList<SerialCode> i){
        i.add(this);
    }
}