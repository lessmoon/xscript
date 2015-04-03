package inter;

import lexer.*;
import symbols.*;

import gen.*;

import java.util.ArrayList;

public class Function extends FunctionBasic {
    public Stmt  stmt;
    private int   f_off = -1;

    public Function(Token n,Type t,ArrayList<Para> p){
        super(n,t,p);
        stmt = null;
    }

    public void init(Token n,Type t,Stmt s,ArrayList<Para> p){
        name = n;
        type = t;
        stmt = s;
        paralist = p;
    }
    
    public Function(Token n,Type t,Stmt s,ArrayList<Para> p){
        super(n,t,p);
        stmt = s;
    }
    
    public void run(){
        stmt.run();
        return;
    }

    public int getPosition(){
        return f_off;
    }
    
    public void emit(BinaryCodeGen bcg){
        bcg.setMode(BinaryCodeGen.EmitMode.CODE_FUNC);
        f_off = bcg.getCurrentPosition();
        stmt.emit(bcg);
        bcg.setMode(BinaryCodeGen.EmitMode.CODE_RUN);
    }
}