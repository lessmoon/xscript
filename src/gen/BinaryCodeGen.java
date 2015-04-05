package gen;

import java.util.ArrayList;

public class BinaryCodeGen {
    /*TODO:Executing information */
    private ArrayList<CodeBasic> const_ = new ArrayList<CodeBasic>();
    private ArrayList<CodeBasic> func   = new ArrayList<CodeBasic>();
    private ArrayList<CodeBasic> run    = new ArrayList<CodeBasic>();
    static private final int MAGIC_CODE = 0XBEACAFFE;
    public enum EmitMode {
        CODE_FUNC,CODE_CONST,
        CODE_RUN;
    }
    private  int  func_pos = 0,run_pos = 0,const_pos = 0,
                  func_id = -1; 
    private  EmitMode mode = EmitMode.CODE_RUN;
    
    public void setMode(EmitMode mode){
        if(this.mode == EmitMode.CODE_FUNC && mode == EmitMode.CODE_RUN){
            func_id ++;
        }
        this.mode = mode;
    }

    public EmitMode getMode(){
        return mode;
    }
    
    public int emit(CodeBasic c){
        if(this.mode == EmitMode.CODE_FUNC){
            func.add(c);
            func_pos += c.size();
            return func_pos;
        } else if(this.mode == EmitMode.CODE_RUN){
            run.add(c);
            run_pos += c.size();
            return run_pos;
        } else {// if(this.mode == EmitMode.CODE_CONST){
            const_.add(c);
            const_pos += c.size();
            return const_pos;
        }
    }

    public int emit(int i){
        return this.emit(new IntegerCode(i));
    }

    public int getCurrentPosition(){
        if(this.mode == EmitMode.CODE_FUNC){
            return func_pos;
        } else if(this.mode == EmitMode.CODE_RUN){
            return run_pos;
        } else {// if(this.mode == EmitMode.CODE_CONST){
             return const_pos;
        }           
    }

    public void serialize(WriteBinaryCode wbc){
        wbc.write(MAGIC_CODE);
        wbc.write(4*4+const_pos);//position of function
        wbc.write(4*4+const_pos + func_pos);//position of entering position
        wbc.write(4*4+const_pos + run_pos);//size of the whole application
        for(CodeBasic i : const_){
            i.writeCode(wbc);
        }
        for(CodeBasic i : func){
            i.writeCode(wbc);
        }
        for(CodeBasic i : run){
            i.writeCode(wbc);
        }
    }
}