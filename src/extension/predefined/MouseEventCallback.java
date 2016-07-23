package extension.predefined;

import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;
import symbols.Type;
import inter.util.Para;
import inter.stmt.MemberFunction;

import java.util.ArrayList;
import java.util.List;

public class MouseEventCallback extends extension.Struct {
    /*
     * return the struct defined by the java code
     */
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable){
        /*
         * struct {
         *      def virtual void callback(int x,int y);
         * }
         */
        final symbols.Struct s = new symbols.Struct(sname);
        Token fname = dic.getOrReserve("callback");
        List<Para> plist = new ArrayList<>();
        plist.add(new Para(s,lexer.Word.This));
        plist.add(new Para( Type.Int,dic.getOrReserve("x") ) );
        plist.add(new Para( Type.Int,dic.getOrReserve("y") ) );
        MemberFunction f = new MemberFunction(fname,Type.Bool,plist,s);
        s.defineVirtualFunction(fname,f);
        return s;
    }
}