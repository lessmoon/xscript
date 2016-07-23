package symbols;

import inter.stmt.FunctionBasic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VirtualTable {
    private final List<List<FunctionBasic>> vtable;

    public VirtualTable(){
        vtable = new ArrayList<>();
    }

    @Override
    public Object clone() {
        VirtualTable t = new VirtualTable();
        t.vtable.addAll(vtable.stream().map(ArrayList::new).collect(Collectors.toList()));
        return t;
    }

    public void createNewTable(){
        vtable.add(new ArrayList<>());
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
        for(List<FunctionBasic> l : vtable){
            for(FunctionBasic m : l){
                if(!m.isCompleted()){
                    return false;
                }
            }
        }
        return true;
    }

}