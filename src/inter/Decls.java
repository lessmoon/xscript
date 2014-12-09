package inter;

import lexer.*;
import symbols.*;
import runtime.*;

import java.util.*;

public class Decls extends Stmt {
    ArrayList<Decl> decls = new ArrayList<Decl>();
    
    public Decls(){
    }
    
    public ArrayList<Decl> getDecls(){
        return decls;
    }

    public int size(){
        return decls.size();
    }

    public void addDecl(Decl c){
        decls.add(c);
    }

    public void run(){
        for(Decl d : decls){
            d.run();
        }
    }
}