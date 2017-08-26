package runtime;

import inter.expr.Value;
import lexer.Token;
import symbols.Env;
import symbols.EnvEntry;
import symbols.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class ConstantReference {
    private Value value;
    ConstantReference(Value value){
        this.value = value;
    }
    public Value getValue(){
        return value;
    }

    public Value setValue(Value value) {
        this.value = value;
        return value;
    }
}

public enum VarTable {
    ;
    static final boolean IS_DEBUG = false;
    static final private List<ConstantReference> globalTable = new ArrayList<>();
    static final private ThreadLocal<Stack<List<ConstantReference>>> table = ThreadLocal.withInitial(Stack::new);
    static final private ThreadLocal<Env> env = new ThreadLocal<>();
    static final private Env global = new Env();
    static boolean NEED_DEBUG = false;

    static private int levels(VarTable r){
        return 1;//r.prev==null?1:levels(r.prev) + 1;
    }

    static public List<Value> getTop(){
        List<Value> arr = new ArrayList<>();
        if(table.get().empty()){
            globalTable.stream().map(ConstantReference::getValue).forEach(arr::add);
        } else {
            table.get().peek().stream().map(ConstantReference::getValue).forEach(arr::add);
        }
        return arr;
    }

    static public void enableDebug(){
        NEED_DEBUG = true;
    }

    static public void popTop(){
        table.get().pop();
        if(NEED_DEBUG){
            assert env.get() != null;
            env.set(env.get().getPrev());
        }
        ///*
        if(IS_DEBUG){
            for(int i = 0 ; i < table.get().size() ; i++)
                System.out.print("  |");
            System.out.println("pop");
        }//*/
    }

    static public void pushTop(){
        if(NEED_DEBUG){
            env.set(env.get()==null?new Env():new Env(env.get()));
        }
        ///*
        if(IS_DEBUG){
            for(int i = 0 ; i < table.get().size() ; i++)
                System.out.print("  |");
            System.out.println("push" );
        }
        //*/
        table.get().push(new ArrayList<>());
    }

    static public void pushVar(Value v){
        ///*
        if(IS_DEBUG){
            for(int i = 0 ; i < table.get().size() - 1 ; i++)
                System.out.print("  |");
            System.out.println("def " + table.get().peek().size());
        }
        //*/
        if(table.get().empty()){
            globalTable.add(new ConstantReference(v));
        } else {
            table.get().peek().add(new ConstantReference(v));
        }
    }

    static public void defVar(Token name, Type t){
        if(NEED_DEBUG){
            if (table.get().empty()) {
                global.put(name,t);
            } else {
                env.get().put(name,t);
            }
        }
    }

    static public Value getVar(int sloff, int offset){
        /*
        for(int i = 0 ; i < sloff ; i++)
            System.out.print("  |");
        System.out.println("get");*/
        int nowlevel = table.get().size() ;
        return getVarAbsolutely(nowlevel - sloff,offset);
    }

    static public Value setVar(int sloff, int offset, Value v){
        /*
        for(int i = 0 ; i < sl ; i++)
            System.out.print("  |");
        System.out.println("set");*/
        int nowlevel = table.get().size() ;
        return setVarAbsolutely(nowlevel - sloff,offset,v);
    }
    
    static public Value getVarAbsolutely(int sl, int offset){
        
        ///*
        if(IS_DEBUG&&false){
            for(int i = 0 ; i < sl ; i++)
                System.out.print("  |");
            System.out.println("get " + offset );
        }//*/
        List<ConstantReference> c = sl == 0 ? globalTable:table.get().get(sl - 1);
        return c.get(offset).getValue();
    }
    
    static public Value setVarAbsolutely(int sl, int offset, Value v){
        ///*
        if(IS_DEBUG&&false){
            for(int i = 0 ; i < sl ; i++)
                System.out.print("  |");
            System.out.println("set " + offset + " = " );
        }//*/
        List<ConstantReference> c = sl == 0 ? globalTable:table.get().get( sl - 1 );
        return c.get(offset).setValue(v);
    }
    static public EnvEntry getVarInfo(Token t){
        if( env.get() != null){
            EnvEntry e = env.get().get(t);
            if(e != null){
                return new EnvEntry(e.type,e.stacklevel + 1 ,e.offset,e.isReadOnly,e.initValue);
            }
        }
        return global.get(t);
    }
    static public void printVarInfo() {
        if(env.get() != null){
            System.out.println(env.get().toString(1));
        }
        System.out.print("[G]");
        System.out.println(global.toString(0));
    }
}