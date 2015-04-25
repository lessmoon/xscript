package inter.stmt;

import lexer.*;
import symbols.*;
import runtime.*;
import inter.code.SerialCode;

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

    @Override
    public void run(){
        for(Decl d : decls){
            d.run();
        }
    }

    @Override
    public Stmt optimize(){
        for(Decl d : decls){
            d.optimize();
        }
        return this;
    }

    @Override
    public void emitCode(ArrayList<SerialCode> i){
        for(Decl d : decls){
            d.emitCode(i);
        }
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        for(Decl d : decls){
            sb.append(d.toString());
        } 
        return sb.toString();
    }
}