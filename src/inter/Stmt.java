package inter;

import runtime.*;

public class Stmt extends Node{
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
    };
    public static final Stmt PushStack = new Stmt(){
        public void run(){
            VarTable.pushTop();
        }
        public String toString(){
            return "PushStack\n";
        }
    };
    
    public String toString(){
        return this.getClass().getName() + "\n";
    }

    public static Stmt Enclosing = Null;
}