package inter;

import runtime.*;
import gen.Reference;

public class Stmt extends Node{

    Reference<Integer> after = new Reference<Integer>();
    Reference<Integer> next  = new Reference<Integer>();

    public Stmt()
    {}

    public void run(){
        
    }

    public static final Stmt Null = new Stmt();
    public static final Stmt RecoverStack = new Stmt(){
        public void run(){
            VarTable.popTop();
        }
        public String toString(){
            return "RecoverStack\n";
        }
        /*
            void emitBinaryCode(BinaryCode x){
                x.emit(POP_STACK);
            }
        */
    };
    public static final Stmt PushStack = new Stmt(){
        public void run(){
            VarTable.pushTop();
        }
        public String toString(){
            return "PushStack\n";
        }
        /*
        public    void emit(BinaryCodeGen bcg){
                x.emit(PUSH_STACK);
            }
        */
    };

    public String toString(){
        return this.getClass().getName() + "\n";
    }

    public Stmt optimize(){
        return this;
    }

    public static Stmt Enclosing = Null;
}