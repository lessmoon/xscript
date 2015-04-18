package inter.stmt;

import runtime.*;

public class Continue extends Stmt {
    static public  final Throwable ContinueCause = new Throwable();
    Stmt stmt;
    public final int sizeOfStack;
    public Continue(int s){
        if( Stmt.Enclosing == Stmt.Null )
            error("unenclosed continue");
        stmt = Stmt.Enclosing;
        sizeOfStack = s;
    }

    public void run(){
        /*
         * I *KNOW* it is wrong use of exception
         * But it works well.
         * Maybe I will change the virtual machine.
         */
        for(int i = 0 ; i < sizeOfStack;i++)
            VarTable.popTop();

        throw new RuntimeException(ContinueCause);
    }
}