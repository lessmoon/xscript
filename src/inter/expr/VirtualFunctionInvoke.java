package inter.expr;

import inter.stmt.FunctionBasic;
import inter.stmt.ReturnResult;
import runtime.RunStack;
import runtime.VarTable;
import symbols.Position;
import symbols.Struct;
import symbols.VirtualTable;

import java.util.List;
import java.util.ListIterator;

public class VirtualFunctionInvoke extends Expr {
    static  final   boolean         IS_DEBUG = false;
    private final   FunctionBasic   func;
    private final   List<Expr>      args;
    private final Expr              expr;
    private final Position          position;/*the function position in virtual table*/

    /**
     * Invoke virtual function of {@code expr}.getValue()
     * @param expr the expression
     * @param function the function signature of the vf
     * @param args the args
     */
    public VirtualFunctionInvoke(Expr expr, FunctionBasic function, List<Expr> args){
        super(function.getName(), function.getType());
        this.expr = expr;
        func = function;
        this.args = args;
        assert(expr.type instanceof Struct);
        Struct t = (Struct)expr.type;
        position = t.getVirtualFunctionPosition(function.getName());
        assert(position != null);
        check();
    }

    void check(){
        if(func.getParamSize() != args.size() + 1)
            error("function parameters number not match:" + func);
        for(int i = 1; i < func.getParamSize(); i++){
            if(!func.getParamInfo(i).type.isCongruentWith(args.get(i - 1).type)){
                Expr e = args.get(i - 1);
                Expr f = ConversionFactory.getConversion(e,func.getParamInfo(i).type);
                assert(f != null);
                args.set(i - 1,f);
            }
        }
    }

    @Override
    public boolean isChangeable(){
        return true;
    }

    @Override
    public Expr optimize(){
        /*may have conversion*/
        ListIterator<Expr> iter = args.listIterator();
        while(iter.hasNext()){
            iter.set(iter.next().optimize());
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
        if(i < args.size()){
            sb.append(args.get(i++).toString());
            while(i < args.size() ){
                sb.append(",");
                sb.append(args.get(i++).toString());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public Value getValue(){
        final Value[] args = new Value[this.args.size() + 1];
        args[0] = expr.getValue();
        if(args[0] == Value.Null){
            error("null pointer error:try to invoke virtual function `" + func + "' of a null pointer");
        }

        assert(args[0] instanceof StructValue);

        VirtualTable vtable = ((StructValue)(args[0])).getVirtualTable();
        FunctionBasic f = vtable.getVirtualFunction(position);

        for(int i = 1 ; i < args.length;i++){
            args[i] = this.args.get(i - 1).getValue();
        }
        VarTable.pushTop();
        int i = 0;
        for(Value c : args){
            if(IS_DEBUG){
                System.out.println("\narg[" + i + "]{" + (i==0?expr:this.args.get(i-1)) + "} = " + c + "<->" + c.hashCode());
                i++;
            }
            VarTable.pushVar(c);
        }
        if(IS_DEBUG){
            System.out.println("\nVirtualInvoke " + func.toString() + "{");
        }
        RunStack.invokeFunction(line, offset, filename, f);
        Value result =  type.getInitialValue();

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