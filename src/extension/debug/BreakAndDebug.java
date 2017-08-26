package extension.debug;

import extension.Function;
import inter.expr.Value;
import lexer.Token;
import runtime.Dictionary;
import runtime.RunStack;
import runtime.TypeTable;
import runtime.VarTable;
import symbols.EnvEntry;

import java.util.List;
import java.util.Scanner;

/**
 * Created by lessmoon on 2017/7/4.
 */
public class BreakAndDebug extends Function{
    Dictionary dic;
    TypeTable typeTable;

    @Override
    public void init(Dictionary dic, TypeTable typeTable) {
        super.init(dic, typeTable);
        this.dic = dic;
        this.typeTable = typeTable;
        VarTable.enableDebug();
    }

    public void process(){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Debug>>>");
            String r = scanner.next();
            switch (r){
                case "q":case "quit":return;
                case "t":case "type":
                    String name = scanner.next();
                    Token t = dic.getOrReserve(name);
                    EnvEntry ee = VarTable.getVarInfo(t);
                    Value v = VarTable.getVarAbsolutely(ee.stacklevel,ee.offset);
                    System.out.print("["+ee.stacklevel+","+ee.offset+"]:"+v.type);
                    if (v.type.isBuiltInType()) {
                       System.out.print(" {" + v + "}") ;
                    }
                    System.out.print("\n");
                    break;
                case "s":case "stack":
                    RunStack.printStackTrace();
                    break;
                case "vs":case "varstack":
                    VarTable.printVarInfo();
                    break;
            }
        }
    }

    @Override
    public Value run(List<Value> args) {
        Value condition = args.get(0).getValue();
        if(condition == Value.True){//if it is true
            process();
        }
        return condition;
    }
}
