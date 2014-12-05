package inter;

import runtime.*;

public class Stmt extends Node{
    public Stmt()
    {}

    public void run(){
        
    }

    public static final Stmt Null = new Stmt();
    public static final Stmt Recover = new Stmt(){
        public void run(){
            VarTable.popTop();
        }
    };
    public static Stmt Enclosing = Null;
}