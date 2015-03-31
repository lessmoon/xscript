package runtime;

import inter.*;
import lexer.*;
import symbols.*;

import java.util.*;

class ConstantReference {
    Constant v;
    ConstantReference(Constant v){
        this.v = v;
    }
}

public class VarTable {
    static final boolean IS_DEBUG = false;
    static private Stack<ArrayList<ConstantReference>> table = new Stack<ArrayList<ConstantReference>>();
    static {
        pushTop();
    }
    
    static private int levels(VarTable r){
        return 1;//r.prev==null?1:levels(r.prev) + 1;
    }

    static public ArrayList<Constant> getTop(){
        ArrayList<Constant> arr = new ArrayList<Constant>();
        for(ConstantReference c : table.peek())
            arr.add(c.v);
        return arr;
    }

    static public void popTop(){
        table.pop();
        ///*
        if(IS_DEBUG){
            for(int i = 0 ; i < table.size() ; i++)
                System.out.print("  |");
            System.out.println("pop");
        }//*/
    }

    static public void pushTop(){
        ///*
        if(IS_DEBUG){
            for(int i = 0 ; i < table.size() ; i++)
                System.out.print("  |");
            System.out.println("push" );
        }
        //*/
        table.push(new ArrayList<ConstantReference>());
    }

    static public void pushVar(Constant v){
        ///*
        if(IS_DEBUG&&false){
            for(int i = 0 ; i < table.size() - 1 ; i++)
                System.out.print("  |");
            System.out.println("def " + table.peek().size());
        }
        //*/
        table.peek().add(new ConstantReference(v));
    }

    static public Constant getVar(int sloff,int offset){
        /*
        for(int i = 0 ; i < sloff ; i++)
            System.out.print("  |");
        System.out.println("get");*/
        int nowlevel = table.size() - 1 ;
        return getVarAbsolutely(nowlevel - sloff,offset);
    }

    static public Constant setVar(int sloff,int offset,Constant v){
        /*
        for(int i = 0 ; i < sl ; i++)
            System.out.print("  |");
        System.out.println("set");*/
        int nowlevel = table.size() - 1 ;
        return setVarAbsolutely(nowlevel - sloff,offset,v);
    }
    
    static public Constant getVarAbsolutely(int sl,int offset){
        
        ///*
        if(IS_DEBUG&&false){
            for(int i = 0 ; i < sl ; i++)
                System.out.print("  |");
            System.out.println("get " + offset );
        }//*/
        ArrayList<ConstantReference> c = table.get(sl);

        return c.get(offset).v;
    }
    
    static public Constant setVarAbsolutely(int sl,int offset,Constant v){
        ///*
        if(IS_DEBUG&&false){
            for(int i = 0 ; i < sl ; i++)
                System.out.print("  |");
            System.out.println("set " + offset + " = " );
        }    //*/
        ArrayList<ConstantReference> c = table.get(sl);
        c.get(offset).v = v;
        return v;
    }
}