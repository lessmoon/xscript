package inter.code;

import runtime.RunEnv;
import inter.expr.Constant;

public class JumpTrueCode implements SerialCode {
    JumpCode jc;

    public JumpTrueCode(JumpCode j){
        this.jc = j;
    }

    public JumpTrueCode(IntReference addr){
        this.jc = new JumpCode(addr);
    }

    @Override
    public void serially_run(RunEnv r){
        if(r.getResult() != Constant.False){
            jc.serially_run(r);
        }
    }
}