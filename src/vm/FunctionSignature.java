package vm;

import symbols.Type;

/**
 * MethodSignature
 */
public interface FunctionSignature {
    public String getDescription();

    public List<Type> getParameters();
    public String getFunctionBaseName();
    public Type getReturnType();
    
    public boolean isNative();
    public boolean isInitialFunction();
    public boolean isGlobal();

    public default boolean isMember() { 
        return !isGlobal();
    }
    public boolean isComplete();
    public boolean isVirtual();
    public boolean isPureVirtual();
}