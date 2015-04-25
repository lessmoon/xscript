package inter.stmt;

import runtime.*;
import inter.util.Node;
import inter.code.IntReference;
import inter.code.*;

import java.util.ArrayList;

public class Stmt extends Node{
    public IntReference headaddr = new IntReference();
    public IntReference tailaddr = new IntReference();

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

        @Override
        public void emitCode(ArrayList<SerialCode> i){
            i.add(StatementCode.RecoverStack);
        }
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

        @Override
        public void emitCode(ArrayList<SerialCode> i){
            i.add(StatementCode.PushStack);
        }
    };

    @Override
    public String toString(){
        return this.getClass().getName() + "\n";
    }

    public Stmt optimize(){
        return this;
    }

    public void setHeadAddr(int h){
        headaddr.setValue(h);
    }

    public void setTailAddr(int t){
        tailaddr.setValue(t);
    }

    public int getHeadAddr(){
        return headaddr.getValue();
    }

    public int getTailAddr(){
        return tailaddr.getValue();
    }

    public void emitCode(ArrayList<SerialCode> i){
        /*Do nothing*/
    }

    public static Stmt Enclosing        = Null;
    public static Stmt BreakEnclosing   = Null; 
}