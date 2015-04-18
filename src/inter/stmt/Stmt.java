package inter.stmt;

import runtime.*;
import inter.util.Node;

public class Stmt extends Node{
    public Stmt()
    {}

    public void run(){
        
    }

    public static final Stmt Null = new Stmt();
    public static final Stmt RecoverStack = new Stmt(){
        @Override
        public void run(){
            VarTable.popTop();
        }

        @Override
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
        @Override
        public void run(){
            VarTable.pushTop();
        }
        @Override
        public String toString(){
            return "PushStack\n";
        }
        /*
            void emitBinaryCode(BinaryCode x){
                x.emit(PUSH_STACK);
            }
        */
    };
    
    @Override
    public String toString(){
        return this.getClass().getName() + "\n";
    }

    public Stmt optimize(){
        return this;
    }

    public static Stmt Enclosing        = Null;
    public static Stmt BreakEnclosing   = Null; 
}