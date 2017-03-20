package inter.stmt;

import runtime.VarTable;

public class Break extends Stmt {
    static public  final Throwable BreakCause = new Throwable();
    Stmt stmt;
    private final int sizeOfStack;
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
	public boolean isLastStmt(){
		return true;
	}    
    /*
        void emitBinaryCode(BinaryCode x){
            x.emit(POPN_STACK);
            x.emit(sizeOfStack);
            Reference<Integer> i  = new Reference<Integer>(x.getCurrentAddress());
            x.emit(JUMPOFF_TO);
            x.emitIntegerOffsetReference(i,body.after);//body.after should be reference
        }
    */
}