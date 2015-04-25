package inter.code;
import runtime.RunEnv;

public class JumpOffCode extends JumpCode {
    public JumpOffCode(IntReference i){
        super(i);
    }

    @Override
    public void serially_run(RunEnv r){
        r.jumpOffset(i.getValue());
    }
}