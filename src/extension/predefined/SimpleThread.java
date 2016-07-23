package extension.predefined;

import extension.Struct;
import inter.expr.Constant;
import inter.expr.StackVar;
import inter.expr.StructConst;
import inter.stmt.FunctionBasic;
import inter.stmt.InitialFunction;
import inter.stmt.MemberFunction;
import inter.stmt.ReturnResult;
import inter.stmt.Stmt;
import inter.util.Para;
import lexer.Token;
import lexer.Word;
import runtime.Dictionary;
import runtime.Interface;
import runtime.TypeTable;
import symbols.Position;
import symbols.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lessmoon on 2016/7/21.
 */
public class SimpleThread extends Struct {
    private static final String runnableStructName = "Runnable";
    private static final String startFunctionName = "start";

    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        final symbols.Struct s = new symbols.Struct(sname);
        Token fName = dic.getOrReserve(startFunctionName);
        List<Para> plist = new ArrayList<>();
        plist.add(new Para(s,lexer.Word.This));


        Stmt start = new Stmt(){
            final StackVar arg0 = new StackVar(lexer.Word.This,s,0,0);

            @Override
            public void run(){
                StructConst s = (StructConst)arg0.getValue();
                Thread t = (Thread) s.getExtension();
                t.start();
            }
        };

        MemberFunction f = new MemberFunction(fName,Type.Bool,start,plist,s);
        s.addNormalFunction(fName,f);
        final Position vfPos = ((symbols.Struct)typeTable.getType(dic.getOrReserve(runnableStructName)))
                                                .getVirtualFunctionPosition(dic.getOrReserve("run"));

        Stmt initialStmt = new Stmt(){
            final StackVar arg0 = new StackVar(lexer.Word.This,s,0,0);
            final StackVar arg1 = new StackVar(null,s,0,1);
            @Override
            public void run(){
                final StructConst s = (StructConst)arg0.getValue();
                final StructConst runnable = (StructConst) arg1.getValue();
                s.setExtension(new Thread(()->{
                        try {
                            List<Constant> args = new ArrayList<>();
                            args.add(runnable);
                            Interface.invokeVirualFunctionOfStruct(runnable, vfPos, args);
                        } catch (ReturnResult e){
                            return;
                        }
                    }));
            }
        };

        List<Para> para = new ArrayList<>();

        para.add(new Para(s,lexer.Word.This));
        para.add(new Para(typeTable.getType(dic.getOrReserve(runnableStructName)),dic.getOrReserve("runnable")));

        FunctionBasic initialFunction = new InitialFunction(Word.This,initialStmt,para,s);

        s.defineInitialFunction(initialFunction);
        return s;
    }
}
