package inter.code;
import runtime.RunEnv;

public class JumpCode implements SerialCode {
    IntReference i;

    public JumpCode(IntReference i){
        this.i = i;
    }

    @Override
    public void serially_run(RunEnv r){
        r.jumpTo(i.getValue());
    }
    
}