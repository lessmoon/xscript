package runtime;

import inter.expr.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class ConstantReference {
    private Constant value;
    ConstantReference(Constant value){
        this.value = value;
    }
    public Constant getValue(){
        return value;
    }

    public Constant setValue(Constant value) {
        this.value = value;
        return value;
    }
}

public class VarTable {
    static final boolean IS_DEBUG = false;
    static final private List<ConstantReference> globalTable = new ArrayList<>();
    static final private ThreadLocal<Stack<List<ConstantReference>>> table = ThreadLocal.withInitial(Stack::new);

    static private int levels(VarTable r){
        return 1;//r.prev==null?1:levels(r.prev) + 1;
    }

    static public List<Constant> getTop(){
        List<Constant> arr = new ArrayList<>();
        if(table.get().empty()){
            globalTable.stream().map(ConstantReference::getValue).forEach(arr::add);
        } else {
            table.get().peek().stream().map(ConstantReference::getValue).forEach(arr::add);
        }
        return arr;
    }

    static public void popTop(){
        table.get().pop();
        ///*
        if(IS_DEBUG){
            for(int i = 0 ; i < table.get().size() ; i++)
                System.out.print("  |");
            System.out.println("pop");
        }//*/
    }

    static public void pushTop(){
        ///*
        if(IS_DEBUG){
            for(int i = 0 ; i < table.get().size() ; i++)
                System.out.print("  |");
            System.out.println("push" );
        }
        //*/
        table.get().push(new ArrayList<>());
    }

    static public void pushVar(Constant v){
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

    static public Constant getVar(int sloff,int offset){
        /*
        for(int i = 0 ; i < sloff ; i++)
            System.out.print("  |");
        System.out.println("get");*/
        int nowlevel = table.get().size() ;
        return getVarAbsolutely(nowlevel - sloff,offset);
    }

    static public Constant setVar(int sloff,int offset,Constant v){
        /*
        for(int i = 0 ; i < sl ; i++)
            System.out.print("  |");
        System.out.println("set");*/
        int nowlevel = table.get().size() ;
        return setVarAbsolutely(nowlevel - sloff,offset,v);
    }
    
    static public Constant getVarAbsolutely(int sl,int offset){
        
        ///*
        if(IS_DEBUG&&false){
            for(int i = 0 ; i < sl ; i++)
                System.out.print("  |");
            System.out.println("get " + offset );
        }//*/
        List<ConstantReference> c = sl == 0 ? globalTable:table.get().get(sl - 1);
        return c.get(offset).getValue();
    }
    
    static public Constant setVarAbsolutely(int sl,int offset,Constant v){
        ///*
        if(IS_DEBUG&&false){
            for(int i = 0 ; i < sl ; i++)
                System.out.print("  |");
            System.out.println("set " + offset + " = " );
        }//*/
        List<ConstantReference> c = sl == 0 ? globalTable:table.get().get( sl - 1 );
        return c.get(offset).setValue(v);
    }
}