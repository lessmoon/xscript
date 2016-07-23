package extension.system;

import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;
import symbols.Struct;
import symbols.Type;

public class extime extends extension.Struct {
    /*
     * return the struct defined by the java code
     */
    @Override
    public Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
       /*
         * struct {
         *      def virtual void callback(int id);
         * }
         */
        final symbols.Struct s = new symbols.Struct(sname);
        Token  mname1 = dic.getOrReserve("hour"),
               mname2 = dic.getOrReserve("minute"),
               mname3 = dic.getOrReserve("second");
        s.addMemberVariable(mname1,Type.Int);
        s.addMemberVariable(mname2,Type.Int);
        s.addMemberVariable(mname3,Type.Int);
        return s;
    }
}