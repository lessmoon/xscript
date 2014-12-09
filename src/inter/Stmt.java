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
    };
    public static final Stmt PushStack = new Stmt(){
        public void run(){
            VarTable.pushTop();
        }
    };
    
    public static Stmt Enclosing = Null;
}