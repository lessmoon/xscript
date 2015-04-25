package inter.code;

import runtime.RunEnv;
import inter.expr.Constant;

public class JumpFalseCode implements SerialCode {
    JumpCode jc;

    public JumpFalseCode(JumpCode j){
        this.jc = j;
    }
 
    public JumpFalseCode(IntReference addr){
        this.jc = new JumpCode(addr);
    }

    @Override
    public void serially_run(RunEnv r){
        if(r.getResult() == Constant.False){
            jc.serially_run(r);
        }
    }
}