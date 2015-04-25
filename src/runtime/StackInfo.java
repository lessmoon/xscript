package runtime;

import inter.code.SerialCode;
import inter.stmt.FunctionBasic;

import java.util.Stack;
import java.util.ArrayList;

public class StackInfo {
    int pc = 0;
    ArrayList<SerialCode> codes = null;
    FunctionBasic id = null;

    public StackInfo(){
    }

    public StackInfo(ArrayList<SerialCode> codes){
        this.codes = codes;
    }

    public StackInfo(int pc,ArrayList<SerialCode> codes){
        this.pc = pc;
        this.codes = codes;
    }

    public SerialCode getCode(){
        if(pc > codes.size()){
            return null;
        }
        return codes.get(pc);
    }

    public void setEnvFunction(FunctionBasic i){
        this.id = i;
    }

    public FunctionBasic getEnvFunction(){
        return id;
    }

    @Override
    public String toString(){
        return (id==null?"___main___":id.toString()) + ":" ;//+ getCode().lineno ;
    }
}