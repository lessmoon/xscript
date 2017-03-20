package inter.stmt;

import java.util.ArrayList;
import java.util.List;

public class Decls extends Stmt {
   private List<Decl> decls;
    
    public Decls(){
        decls = new ArrayList<>();
    }
    
    public List<Decl> getDecls(){
        return decls;
    }

    public int size(){
        return decls.size();
    }

    public void addDecl(Decl c){
        decls.add(c);
    }

    @Override
    public void run(){
        decls.forEach(Decl::run);
    }

    @Override
    public Stmt optimize(){
        decls.forEach(Decl::optimize);
        return this;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Decl d : decls){
            sb.append(d.toString());
        } 
        return sb.toString();
    }

    @Override
    public void appendToSeq(LinkedSeq s) {
        decls.forEach(s::appendStmt);
    }
}