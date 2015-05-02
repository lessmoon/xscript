package runtime;

import inter.stmt.FunctionBasic;

import java.util.Stack;

class Entry{
    int           line;
    String        filename;
    FunctionBasic invokedFunction;

    public Entry(int line,String file,FunctionBasic func){
        this.line = line;
        filename = file;
        invokedFunction = func;
    }
}

public class RunStack {
    static private final Stack<Entry> run_stack = new Stack<Entry>();
    static private final int MAX_STACK_SIZE     = 128;
    static private final int PRINT_SIZE         = 15;

    static public void invokeFunction(int line,String file,FunctionBasic func){
        run_stack.push(new Entry(line,file,func));
        if(MAX_STACK_SIZE <= run_stack.size()){
            System.err.println("Runtime error:");
            printStackTrace("\tcalling stack is too deep",PRINT_SIZE);
            System.err.println(" .... ");
            System.exit(-1);
        }
    }
    
    static public void endInvokeFunction(){
        run_stack.pop();
    }

    static public void printStackTrace(String errinfo){
        System.err.println(errinfo);
        while (!run_stack.empty()) {
            Entry e = run_stack.pop();
            System.err.println("  in `" + e.invokedFunction + "',called at \"" + e.filename + "\","+ e.line);
        } 
    }

    static public void printStackTrace(String errinfo,int size){
        System.err.println(errinfo);
        while (!run_stack.empty() && size-- >0 ) {
            Entry e = run_stack.pop();
            System.err.println("  in `" + e.invokedFunction + "',called at \"" + e.filename + "\","+ e.line);
        } 
    }
    
}