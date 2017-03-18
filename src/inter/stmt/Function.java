package inter.stmt;

import inter.util.Param;
import lexer.Token;
import symbols.Type;

import java.util.List;

public class Function extends FunctionBasic {

    private Stmt body;

    public Function(Token n, Type t, List<Param> p) {
        super(n,t,p);
        body = null;
    }

    public Function(Token n, Type t, List<Param> p, Stmt body) {
        super(n,t,p);
        this.body = body;
    }

    public final void setBody(Stmt body) {
        this.body = body;
    }

    public final Stmt getBody() {
        return body;
    }

    @Override
    public final void run(){
        body.run();
    }
    
    @Override
    public boolean isCompleted(){
        return body != null;
    }
    
}