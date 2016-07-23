package inter.expr;

import inter.stmt.FunctionBasic;
import inter.stmt.ReturnResult;
import runtime.RunStack;
import runtime.VarTable;
import symbols.Position;
import symbols.Struct;
import symbols.VirtualTable;

import java.util.List;

public class VirtualFunctionInvoke extends Expr {
    static  final   boolean         IS_DEBUG = false;
    private final   FunctionBasic   func;
    private final   List<Expr>      para;
    private final Expr              expr;
    private final Position          position;/*the function position in virtual table*/
    
    public VirtualFunctionInvoke(Expr e, FunctionBasic f, List<Expr> p){
        super(f.name,f.type);
        expr = e;
        func = f;
        para = p;
        assert(e.type instanceof Struct);
        Struct t = (Struct)e.type;
        position = t.getVirtualFunctionPosition(f.name);
        assert(position != null);
        check();
    }

    void check(){
        if(func.getParaNumber() != para.size() + 1)
            error("function parameters number not match:" + func);
        for(int i = 1 ; i < func.getParaNumber(); i++){
            if(!func.getParaInfo(i).type.equals(para.get(i - 1).type)){
                Expr e = para.get(i - 1);
                Expr f = ConversionFactory.getConversion(e,func.getParaInfo(i).type);
                assert(f != null);
                para.set(i - 1,f);
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
        StringBuilder sb = new StringBuilder();
        sb.append(expr.toString());
        sb.append(".[virtual]");
        sb.append(op);
        sb.append( "(");
        int i = 0;
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
        
        final Constant[] args = new Constant[para.size() + 1];
        args[0] = expr.getValue();
        if(args[0] == Constant.Null){
            error("null pointer error:try to invoke virtual function `" + func + "' of a null pointer");
        }

        assert(args[0] instanceof StructConst);

        VirtualTable vtable = ((StructConst)(args[0])).getVirtualTable();
        FunctionBasic f = vtable.getVirtualFunction(position);

        for(int i = 1 ; i < args.length;i++){
            args[i] = para.get(i - 1).getValue();
        }
        VarTable.pushTop();
        int i = 0;
        for(Constant c : args){
            if(IS_DEBUG){
                System.out.println("\narg[" + i + "]{" + para.get(i) + "} = " + c + "<->" + c.hashCode());
                i++;
            }
            VarTable.pushVar(c);
        }
        if(IS_DEBUG){
            System.out.println("\nVirtualInvoke " + func.toString() + "{");
        }
        RunStack.invokeFunction(lexline,filename,f);
        Constant result =  type.getInitialValue();

        try {
            f.run();
            if(IS_DEBUG){
                System.out.println("\n}End VirtualInvoke#2 " + func.toString());
            }
        } catch(ReturnResult e){
            if(IS_DEBUG){
                System.out.println("\n}End VirtualInvoke#1 " + func.toString());
            }
            result =  e.value;
        }
        RunStack.endInvokeFunction();
        VarTable.popTop();
        return result;
    }
}