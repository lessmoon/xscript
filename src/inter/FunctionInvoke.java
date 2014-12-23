package inter;

import lexer.*;
import symbols.*;
import runtime.*;

import java.util.ArrayList;

public class FunctionInvoke extends Expr {
    FunctionBasic        func;
    ArrayList<Expr>      para;

    public FunctionInvoke(FunctionBasic f,ArrayList<Expr> p){
        super(f.name,f.type);
        
        func = f;
        para = p;
        check();
    }

    void check(){
        if(func.getParaNumber() != para.size())
            error("function parameters number not match:" + func);
        for(int i = 0 ; i < func.getParaNumber(); i++){
            if(!func.getParaInfo(i).type.equals(para.get(i).type)){
                Expr e = para.get(i);
                Expr f = ConversionFactory.getConversion(e,func.getParaInfo(i).type);
                if(f == null){
                    error("function `" + func + "' parameter type not match:`" + func.getParaInfo(i).name + "' need " + func.getParaInfo(i).type + " actually " + para.get(i).type);
                }
                para.set(i,f);
            }
        }
    }

    boolean isChangeable(){
        return true;
    }

    public Expr optimize(){
        return this;
    }

    public String toString(){
        return op.toString();
    }

    public Constant getValue(){
        try {
            System.out.println("Invoke " + func.toString());
            VarTable.pushTop();
            for(int i = 0 ; i < para.size();i++){
                VarTable.pushVar(para.get(i).getValue());
            }
            func.run();
        } catch(ReturnResult e){
            VarTable.popTop();
            System.out.println("End Invoke#1 " + func.toString());
            return e.value;
        }
        VarTable.popTop();
        System.out.println("End Invoke#2 " + func.toString());
        return null;
    }
}