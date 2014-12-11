package inter;

import lexer.*;
import symbols.*;
import inter.Constant;
import inter.FunctionBasic;
import inter.Para;
import runtime.VarTable;

import java.util.ArrayList;
 
public class ExFunction extends FunctionBasic {
    public extension.Function func;
    ArrayList<Constant> paras = new ArrayList<Constant>();
    public ExFunction(Type t,Token n,ArrayList<Para> pl,extension.Function f){
        super(n,t,pl);
        func = f;
    }

    public void run(){
        VarTable top = VarTable.getTop();
        paras.clear();
        for(int i = 0 ; i < paralist.size();i++){
            paras.add(top.getVar(getParaInfo(i).name));
        }
        throw new ReturnResult(func.run(paras));
    }
}