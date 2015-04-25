package inter.code;

import inter.stmt.Stmt;
import runtime.RunEnv;

public class StatementCode implements SerialCode {
    Stmt s = null;
    static public final StatementCode 
        RecoverStack = new StatementCode(Stmt.RecoverStack),
        PushStack    = new StatementCode(Stmt.PushStack);
    
    
    public StatementCode(Stmt s){
        this.s = s;
    }
    
    public void serially_run(RunEnv r){
        s.run();
    }
}