package symbols;

import lexer.*;
import inter.stmt.FunctionBasic;

import java.util.ArrayList;

public class VirtualTable {
    private final ArrayList< ArrayList<FunctionBasic> > vtable;

    public VirtualTable(){
        vtable = new ArrayList< ArrayList<FunctionBasic> >();
    }

    @Override
    public Object clone(){
        VirtualTable t = new VirtualTable();
        for(ArrayList<FunctionBasic> l : vtable){
            t.vtable.add(new ArrayList<FunctionBasic>(l));
        }
        return t;
    }

    public void createNewTable(){
        vtable.add(new ArrayList<FunctionBasic>());
    }

    public int getGenerations(){
        return vtable.size();
    }

    public int getTopSize(){
        int last = vtable.size() - 1; 
        return vtable.get(last).size();
    }

    public void addVirtualFunction(FunctionBasic f){
        int last = vtable.size() - 1; 
        vtable.get(last).add(f);
    }

    public void overrideVirtualFunction(int g,int i,FunctionBasic m){
        vtable.get(g).set(i,m);
    }

    public FunctionBasic getVirtualFunction(int g,int i){
        return vtable.get(g).get(i);
    }

    public FunctionBasic getVirtualFunction(Position p){
        return getVirtualFunction(p.generation,p.index);
    }

    public boolean isCompleted(){
        for(ArrayList<FunctionBasic> l : vtable){
            for(FunctionBasic m : l){
                if(!m.isCompleted()){
                    return false;
                }
            }
        }
        return true;
    }

}