package symbols;

import inter.expr.Constant;

public class EnvConstEntry extends EnvEntry {
    public final Constant value;
    
    public EnvConstEntry(Constant value){
        super(value.type,-1,-1);//(-1,-1) (sl,o) for constant value
        this.value = value;
    }
    
    
}