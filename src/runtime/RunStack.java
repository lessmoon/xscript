package runtime;

import inter.stmt.FunctionBasic;

import java.util.Stack;

class Entry{
    int           line;
    int offset;
    String        filename;
    FunctionBasic invokedFunction;

    Entry(int line, int offset, String file, FunctionBasic func){
        this.line = line;
        filename = file;
        this.offset = offset;
        invokedFunction = func;
    }
}

public enum RunStack {
    ;
    static private final ThreadLocal<Stack<Entry>> run_stack = ThreadLocal.withInitial(Stack::new);
    static private final int MAX_STACK_SIZE     = 128;
    static private final int PRINT_SIZE         = 15;

    static public void invokeFunction(int line, int offset, String file, FunctionBasic func){
        run_stack.get().push(new Entry(line,offset,file,func));
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
            System.err.println("  in `" + e.invokedFunction + "',called at \"" + e.filename + "\","+ e.line +":" + e.offset);
        } 
    }

    static public void printStackTrace(String errinfo,int size){
        System.err.println(errinfo);
        while (!run_stack.get().empty() && size-- > 0 ) {
            Entry e = run_stack.get().pop();
            System.err.println("  in `" + e.invokedFunction + "',called at \"" + e.filename + "\","+ e.line + ":" + e.offset);
        } 
    }
    
}