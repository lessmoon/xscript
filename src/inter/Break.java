package inter;

public class Break extends Stmt {
    static public  final Throwable BreakCause = new Throwable();
    Stmt stmt;
    public Break(){
        if( Stmt.Enclosing == Stmt.Null )
            error("unenclosed break");
        stmt = Stmt.Enclosing;
    }

    public void run(){
        /*
         * I *KNOW* it is wrong use of exception
         * But it works well.
         * Maybe I will change the virtual machine.
         */
        throw new RuntimeException(BreakCause);
    }
}