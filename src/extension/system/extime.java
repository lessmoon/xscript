package extension.system;

import lexer.Token;
import runtime.Dictionary;
import symbols.Type;
import inter.util.Para;
import inter.stmt.Stmt;
import inter.expr.Var;
import inter.expr.StructConst;
import inter.stmt.MemberFunction;

import java.util.ArrayList;

public class extime extends extension.Struct {
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
        Token mname1 = dic.getOrreserve("hour"),
			  mname2 = dic.getOrreserve("minute"),
			  mname3 = dic.getOrreserve("second");
        s.addMemberVariable(mname1,Type.Int);
		s.addMemberVariable(mname2,Type.Int);
		s.addMemberVariable(mname3,Type.Int);
        return s;
    }
}