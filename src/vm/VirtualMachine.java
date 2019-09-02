package vm;

import inter.expr.Value;

public interface VirtualMachine {
    /**
     * set program count of this runtime(for every thread)
     * @param count
     */
    void setProgramCount(int count);
    /**
     * set program count to now + offset 
     * @param offset
     */
    void addProgramCount(int offset);

    /**
     * get program count
     * @return offset
     */
    int getProgramCount();

    /**
     * pop the top level of variable stack 
     */
    void popStackFrame();
    /**
     * push one 
     */
    void pushStackFrame();

    /**
     * push Variables of stacks
     * @param value
     */
    void pushValueAtFrame(Value value);

    /**
     * pop variable of stacks
     * @return the variable value
     */
    Value popValueAtFrame();

    /**
     * invoke a normal function(not virtual call)
     * the return value will be saved to Pointer
     * @param functionId the function id
     */
    void invokeFunction(int functionId);

    /**
     * invoked function returns
     */
    void endInvokeFunction();

    /**
     * invoke a virtual function of value, position shows which function in its' vtable
     * @param value the virtual function
     * @param position the position
     */
    void invokeVirtualFunction(Value value, int position);

    /**
     * set register by value
     * @param id the register id
     * @param value value to set register with
     */
    void setRegister(int id, Value value);

    /**
     * get register by id
     * @param id the register id
     * @return the register value
     */
    Value getRegister(int id);
    /**
     * save register to stack
     */
    void saveRegister();
    /**
     * pop registers from stack
     */
    void popRegister();

    /**
     * register size
     */
    int registerSize();

    /**
     * clear all register
     */
    void clearRegister();

    /**
     * get value with absolute position
     * @param pos the variable pointer
     * @return the value of this position
     */
    Value getValueAbsolute(Position pos);

    /**
     * get value with relative frame offset
     * @param pos the relative pointer (only relative to frame)
     * @return the value of this position
     */
    Value getValueOffset(Position pos);

    /**
     * set value by absolute frame offset
     * @param pos the absolute position (only relative to frame)
     * @param value the value to set variable with
     */
    void setValueAbsolute(Position pos, Value value);

    /**
     * set value by relative frame offset
     * @param pos the relative pointer (only relative to frame)
     * @param value the value to set variable with
     */
    void setValueOffset(Position pos, Value value);

    /**
     * get value of global variable table
     * @param offset variable offset of the frame
     * @return the value of this position
     */
    Value getValueOfGlobal(int offset);

    /**
     * set value of global variable table
     * @param offset the varibale offset of the global frame
     * @param value the value of this position
     */
    void setValueOfGlobal(int offset, Value value);


    /**
     * raise an error
     * @param error the informations
     */
    void raiseException(Object error);

    /**
     * run one instruction
     * @return {@code false} if end running, else {@code true}
     */
    boolean step();
}