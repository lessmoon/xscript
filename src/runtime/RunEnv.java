package runtime;

import inter.expr.Constant;
import inter.code.SerialCode;
import inter.stmt.FunctionBasic;

import java.util.Stack;
import java.util.ArrayList;


public class RunEnv {
    public Stack<StackInfo> stack = new Stack<StackInfo>();
    public Constant res = null;
    public StackInfo info;
    public Stack<Constant> tmp = new Stack<Constant>()
    
    public RunEnv(ArrayList<SerialCode> codes){
        info = new StackInfo(codes);
    }

    public void functionReturn(){
        System.out.println("Return:" + info.getEnvFunction());
        info = stack.pop();
        
    }

    public void functionCall(ArrayList<SerialCode> codes){
        System.out.println("Call:");
        stack.push(info);
        info = new StackInfo(-1,codes);
    }

    public void setEnvFunction(FunctionBasic i){
        System.out.println("" + i);
        info.setEnvFunction(i);
    }
 
    public void jumpTo(int id){
        info.pc = id - 1;
    }

    public void jumpOffset(int id){
        info.pc += id - 1;
    }

    public void setResult(Constant res){
        this.res = res;
    }
    
    public Constant getResult(){
        return this.res;
    }
    
    public SerialCode getCode(){
        return info.getCode();
    }

    public void incPC(){
        info.pc ++;
    }
    
    public Constant top(){
        return tmp.top();
    }
    
    public Constant pop(){
        return tmp.pop();
    }

    public void push(Constant c){
        tmp.push(c);
    }
    
    public void printStackInfo(){
        int l = 0;
        for(StackInfo i : stack){
            System.out.println("" + (l++) + ":" + i);
        }
        System.out.println("" + (l++) + ":"+info);
    }
}