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
                    error("function parameter type not match:" + func.getParaInfo(i).name + " need " + para.get(i).type + " actually " + e.type);
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
        //System.out.println("Invoke " + func.toString());
        try {
            VarTable top = VarTable.pushTop();
            for(int i = 0 ; i < para.size();i++){
                top.pushVar(func.getParaInfo(i).name,para.get(i).getValue());
            }
            func.run();
        } catch(ReturnResult e){
            VarTable.popTop();
            //System.out.println("End Invoke#1 " + func.toString());
            return e.value;
        }
        //System.out.println("End Invoke#2 " + func.toString());
        VarTable.popTop();
        return null;
    }
}