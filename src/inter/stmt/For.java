package inter.stmt;

import inter.expr.Expr;
import inter.expr.Value;
import symbols.Type;

public class For extends Stmt {
    private Stmt begin = null;
    private Expr condition = null;
    private Stmt end = null;
    public Stmt stmt = null;
    public For(){}

    public void init(Stmt b,Expr c,Stmt e,Stmt s){
        begin = b;
        condition = c;
        end = e;
        stmt = s;
        if(check(c.type) == null){
            error("Condition's type should be  bool");
        }
    }
    
    private Type check(Type c){
        return c != Type.Bool?null:c;
    }

    @Override
    public void run(){
        for(begin.run();
        condition.getValue() != Value.False;
        end.run()){
            try{
                stmt.run();
            }catch(RuntimeException e){
                if(e.getCause() == Break.BreakCause)
                    break;
                else if(e.getCause() == Continue.ContinueCause)
                    continue;
                else
                    throw e;
            }
        }
    }

    @Override
    public Stmt optimize(){
        begin = begin.optimize();
        if(condition == Value.False){
            //just remain the begin,and the condition
            return begin;
        } else if(condition == Value.True){/*TODO*/
            
        }
        end = end.optimize();
        stmt = stmt.optimize();
        return this;
    }

    /**
     *  TODO: 2017/3/10
     *  optimize to unfolding the loop
     */
    @Override
    public void appendToSeq(LinkedSeq s) {
        super.appendToSeq(s);
        //begin.appendToSeq(s);
        //new ExprStmt(condition).appendToSeq(s);
        //stmt.appendToSeq(s);
        //end.appendToSeq(s);
    }

    @Override
    public String toString(){
        return "for(" + begin + ";" + condition + ";" + end + "){\n"
                + stmt
                +"}\n";
    }
    
    /*
        void emitBinaryCode(BinaryCode x){
            begin.emit(x);
            int c = x.getCurrentAddress();
            condition.emit(x);
            int e = x.getCurrentAddress();
            x.emit(JFAILED);
            x.emitIntegerReference(Reference<Integer>(after));
            x.emit(JOFF);
            x.emitIntegerReference(Reference<Integer>(s));
            end.emit(x);
            int s = x.getCurrentAddress();
            stmt.emit(x);
            int p = x.getCurrentAddress();
            x.emit(JOFF);
            x.emitIntegerOffsetReference(Reference<Integer>(c),p);  
            int after = x.getCurrentAddress();
        }
    */
}