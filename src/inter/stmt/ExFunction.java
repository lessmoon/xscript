package inter.stmt;

import lexer.Token;
import inter.util.Param;
import runtime.VarTable;
import symbols.Type;

import java.util.List;

public class ExFunction extends FunctionBasic {
    private extension.Function func;

    public ExFunction(Type t, Token n, List<Param> pl, extension.Function f) {
        super(n, t, pl);
        func = f;
    }

    @Override
    public void run() {
        try {
            throw new ReturnResult(func.run(VarTable.getTop()));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}