package inter.stmt;

import lexer.Token;
import inter.util.Para;
import runtime.VarTable;
import symbols.Type;

import java.util.List;

public class ExFunction extends FunctionBasic {
    private extension.Function func;
    public ExFunction(Type t, Token n, List<Para> pl, extension.Function f){
        super(n,t,pl);
        func = f;
    }

    @Override
    public void run(){
        throw new ReturnResult(func.run(VarTable.getTop()));
    }
    
    @Override
    public boolean isCompleted(){
        return true;
    }
}