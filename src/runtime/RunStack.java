package runtime;

import inter.stmt.FunctionBasic;

import java.util.Stack;

class Entry{
    int           line;
    String        filename;
    FunctionBasic invokedFunction;

    Entry(int line, String file, FunctionBasic func){
        this.line = line;
        filename = file;
        invokedFunction = func;
    }
}

public class RunStack {
    static private final ThreadLocal<Stack<Entry>> run_stack = new ThreadLocal<Stack<Entry>>(){
        @Override
        protected Stack<Entry> initialValue(){
            return new Stack<>();
        }
    };
    static private final int MAX_STACK_SIZE     = 128;
    static private final int PRINT_SIZE         = 15;

    static public void invokeFunction(int line,String file,FunctionBasic func){
        run_stack.get().push(new Entry(line,file,func));
        if(MAX_STACK_SIZE <= run_stack.get().size()){
            System.err.println("Runtime error:");
            printStackTrace("\tcalling stack is too deep",PRINT_SIZE);
            System.err.println(" .... ");
            System.exit(-1);
        }
    }
    
    static public void endInvokeFunction(){
        run_stack.get().pop();
    }

    static public void printStackTrace(String errinfo){
        System.err.println(errinfo);
        while (!run_stack.get().empty()) {
            Entry e = run_stack.get().pop();
            System.err.println("  in `" + e.invokedFunction + "',called at \"" + e.filename + "\","+ e.line);
        } 
    }

    static public void printStackTrace(String errinfo,int size){
        System.err.println(errinfo);
        while (!run_stack.get().empty() && size-- > 0 ) {
            Entry e = run_stack.get().pop();
            System.err.println("  in `" + e.invokedFunction + "',called at \"" + e.filename + "\","+ e.line);
        } 
    }
    
}