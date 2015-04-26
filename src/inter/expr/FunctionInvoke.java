package inter.expr;

import lexer.*;
import symbols.*;
import runtime.*;
import inter.stmt.FunctionBasic;
import inter.stmt.MemberFunction;
import inter.stmt.ReturnResult;

import java.util.ArrayList;

public class FunctionInvoke extends Expr {
    static final boolean IS_DEBUG = false;
    FunctionBasic        func;
    ArrayList<Expr>      para;
    
    /*
     * NOTE:(fixed)
     * Wrong when recursively call itself
     * It may rewrite the args in another calling
     */
    //final Constant[]     args;
    
    public FunctionInvoke(FunctionBasic f,ArrayList<Expr> p){
        super(f.name,f.type);
        func = f;
        para = p;
        check();
        f.setUsed();
        //args = new Constant[p.size()];
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

    @Override
    boolean isChangeable(){
        return true;
    }

    @Override
    public Expr optimize(){
        
        /*may have conversion*/
        for(int i = 0 ; i < para.size();i++){
            para.set(i,para.get(i).optimize());
        }
        return this;
    }

    @Override
    public String toString(){
        int i = 0;
        StringBuffer sb = new StringBuffer();
        if(func instanceof MemberFunction){
            sb.append(para.get(i++).toString());
            sb.append(".");
        }
        sb.append(op);
        sb.append( "(");
        if(i < para.size()){
            sb.append(para.get(i++).toString());
            while(i < para.size() ){
                sb.append(",");
                sb.append(para.get(i++).toString());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public Constant getValue(){
        Constant result = null;
        final Constant[] args = new Constant[para.size()];
        for(int i = 0 ; i < args.length;i++){
            args[i] = para.get(i).getValue();
        }

        VarTable.pushTop();
        int i = 0;
        for(Constant c : args){
            
            if(IS_DEBUG){
                System.out.println("\narg[" + i + "]{" + para.get(i) + "} = " + c + "<->" + c.hashCode());
            }
            VarTable.pushVar(c);
            i++;
        }
        if(IS_DEBUG){
            System.out.println("\nInvoke " + func.toString() + "{");
        }
        try {
            func.run();
            if(IS_DEBUG){
            System.out.println("\n}End Invoke#2 " + func.toString());
            }
        } catch(ReturnResult e){
            if(IS_DEBUG){
            System.out.println("\n}End Invoke#1 " + func.toString());
            }
            result =  e.value;
        }
        VarTable.popTop();
        return result;
    }
}