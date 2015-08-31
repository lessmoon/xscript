package extension.predefined;

import lexer.Token;
import runtime.Dictionary;
import symbols.Type;
import inter.util.Para;
import inter.stmt.Stmt;
import inter.expr.Var;
import inter.expr.StructConst;
import inter.stmt.MemberFunction;

import java.util.ArrayList;

public class EventCallback extends extension.Struct {
    /*
     * return the struct defined by the java code
     */
    public symbols.Struct setup(Token sname,Dictionary dic){
        /*
         * struct {
         *      def virtual void callback(int id);
         * }
         */
        final symbols.Struct s = new symbols.Struct(sname);
        Token fname = dic.getOrreserve("callback");
        ArrayList<Para> plist = new ArrayList<Para>();
        plist.add(new Para(s,lexer.Word.This));
        plist.add(new Para( Type.Int,dic.getOrreserve("id") ) );
        MemberFunction f = new MemberFunction(fname,Type.Bool,plist,s);
        s.defineVirtualFunction(fname,f);
        return s;
    }
}