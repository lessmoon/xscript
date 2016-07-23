package extension.predefined;

import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;
import symbols.Struct;
import symbols.Type;
import inter.util.Para;
import inter.stmt.MemberFunction;

import java.util.ArrayList;
import java.util.List;

public class EventCallback extends extension.Struct {
    @Override
    public Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        /*
         * struct {
         *      def virtual void callback(int id);
         * }
         */
        final symbols.Struct s = new symbols.Struct(sname);
        Token fname = dic.getOrReserve("callback");
        List<Para> plist = new ArrayList<>();
        plist.add(new Para(s,lexer.Word.This));
        plist.add(new Para( Type.Int,dic.getOrReserve("id") ) );
        MemberFunction f = new MemberFunction(fname,Type.Bool,plist,s);
        s.defineVirtualFunction(fname,f);
        return s;
    }
}