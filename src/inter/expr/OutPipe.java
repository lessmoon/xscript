package inter.expr;

public class OutPipe extends Expr{
    InPipe inpipe;

    public OutPipe(InPipe inpipe){
        super(inpipe.op,inpipe.type);
        this.inpipe = inpipe;
    }
    
    @Override
    public Expr optimize(){
        Expr x = inpipe.optimize();
        if(x.isChangeable()){
            return this;
        }
        return x.getValue();
    }

    @Override
    public boolean isChangeable(){
        return inpipe.isChangeable();
    }

    @Override
    public Constant getValue(){
        return inpipe.getPipeValue();
    }
}