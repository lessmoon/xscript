package inter.expr;

import inter.stmt.FunctionBasic;
import inter.stmt.InitialFunction;
import inter.stmt.MemberFunction;
import inter.stmt.ReturnResult;
import runtime.RunStack;
import runtime.VarTable;
import symbols.Struct;
import vm.Compiler;
import vm.Pointer;
import vm.Compiler.TemporaryState;

import java.util.List;

public class FunctionInvoke extends Expr {
    private static final boolean IS_DEBUG = false;
    private FunctionBasic functions;
    private final List<Expr> arguments;
    private final boolean isMember;
    /*
     * NOTE:(fixed) Wrong when recursively call itself It may rewrite the args in
     * another calling
     */
    // final Value[] args;

    public FunctionInvoke(FunctionBasic function, List<Expr> arguments) {
        super(function.getName(), function.getType());
        functions = function;
        isMember = function instanceof MemberFunction || function instanceof InitialFunction;
        this.arguments = arguments;
        check();
        function.setUsed();
    }

    void check() {
        if (functions.getParamSize() != arguments.size())
            error("function parameters number not match:" + functions);
        for (int i = 0; i < functions.getParamSize(); i++) {
            if (!functions.getParamInfo(i).type.isCongruentWith(arguments.get(i).type)) {
                Expr e = arguments.get(i);
                Expr f = ConversionFactory.getConversion(e, functions.getParamInfo(i).type);
                assert (f != null);/* won't happen */
                arguments.set(i, f);
            }
        }
    }

    @Override
    public boolean isChangeable() {
        return true;
    }

    @Override
    public Expr optimize() {

        /* may have conversion */
        for (int i = 0; i < arguments.size(); i++) {
            arguments.set(i, arguments.get(i).optimize());
        }
        return this;
    }

    @Override
    public String toString() {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        if (functions instanceof MemberFunction) {
            sb.append(arguments.get(i++).toString());
            sb.append(".");
        } else if (functions instanceof InitialFunction) {
            sb.append(((InitialFunction) functions).getStruct().toString());
            sb.append(".");
        }
        sb.append(op);
        sb.append("(");
        if (i < arguments.size()) {
            sb.append(arguments.get(i++).toString());
            while (i < arguments.size()) {
                sb.append(",");
                sb.append(arguments.get(i++).toString());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public Value getValue() {
        Value result = type.getInitialValue();
        final Value[] args = new Value[arguments.size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = arguments.get(i).getValue();
        }
        if (isMember && args[0] == Value.Null) {
            error("null pointer error: try to invoke function `" + functions + "' of a null pointer");
        }
        VarTable.pushTop();
        int i = 0;
        for (Value c : args) {
            if (IS_DEBUG) {
                System.out.println("\narg[" + i + "]{" + arguments.get(i) + "} = " + c + "<->" + c.hashCode());
            }
            VarTable.pushVar(c);
            VarTable.defVar(functions.getParamInfo(i).name, functions.getParamInfo(i).type);
            i++;
        }
        if (IS_DEBUG) {
            System.out.println("\nInvoke " + functions.toString() + "{");
        }
        RunStack.invokeFunction(line, offset, filename, functions);
        try {
            functions.run();
            if (IS_DEBUG) {
                System.out.println("\n}End Invoke#2 " + functions.toString());
            }
        } catch (ReturnResult e) {
            if (IS_DEBUG) {
                System.out.println("\n}End Invoke#1 " + functions.toString());
            }
            result = e.value;
        }
        RunStack.endInvokeFunction();
        VarTable.popTop();
        return result;
    }

    @Override
    public Pointer compile(Compiler compiler) {
        int id;
        if (isMember) {
            id = compiler.getFunctionIdByName(functions.getName().toString());
        } else {
            Struct s = ((StructFunction)functions).getStruct();
            id = compiler.getMemberFunctionIdByName(s.getName().toString(), functions.getName().toString());
        }
        Pointer[] argsValue = new Pointer[arguments.size()];
        //TODO: emit member function check instruction.
        boolean[] usage = compiler.getRegisterUsage();
        TemporaryState state = compiler.getTemporaryVariableUsage();
        for (int i = 0; i < arguments.size(); i++) {
            argsValue[i] = arguments.get(i).compile(compiler);
            compiler.setRegisterUsage(usage);
            compiler.setTemporaryVariableUsage(state);
        }
        //TODO: emit save stack operator
        //compiler.emitInstruction(new SaveStack())
        //passing arguments frame
        //compiler.emitInstruction(new PushStack())
        //function invoke
        //compiler.emitInstruction(new invoke())
        //function ret
        //compiler.emitInstruction(new StackRecover())
        //return value will return to r#0
        assert !compiler.isRegisterUsed(0);
        return compiler.acquireRegister(0);
    }
}