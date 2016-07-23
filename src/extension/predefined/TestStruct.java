package extension.predefined;

import extension.Struct;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;
import symbols.Type;
import inter.util.Para;
import inter.stmt.Stmt;
import inter.expr.StackVar;
import inter.expr.StructConst;
import inter.stmt.MemberFunction;

import java.util.ArrayList;
import java.util.List;

public class TestStruct extends Struct {
    /*
     * return the struct defined by the java code
     */
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable){
        
        final symbols.Struct s = new symbols.Struct(sname);
        s.addMemberVariable(dic.getOrReserve("id"),Type.Str);
        Token fname = dic.getOrReserve("getId");
        List<Para> plist = new ArrayList<>();
        plist.add(new Para(s,lexer.Word.This));
        /*It is just virtual!*/
        Stmt st = new Stmt(){
            final StackVar arg0 = new StackVar(lexer.Word.This,s,0,0);
            
            @Override
            public void run(){
                StructConst s = (StructConst)arg0.getValue();
                ret(s.getElement(0));
            }
        };
        MemberFunction f = new MemberFunction(fname,Type.Str,st,plist,s);
        s.defineVirtualFunction(fname,f);
        return s;
    }
}