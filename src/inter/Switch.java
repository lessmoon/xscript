package inter;

import lexer.*;
import symbols.*;

import java.util.HashMap;

public abstract class Switch extends Stmt {
    Expr condition;
    Stmt defaultStmt = Stmt.Null;
    Seq stmts = new Seq(Stmt.Null,Stmt.Null);

    public Switch(Expr c){
        condition = c;
    }
    
    public abstract boolean appendCase(Constant c,Stmt s);
    public abstract boolean isCaseSet(Constant c);
    public boolean isDefaultSet(){
        return defaultStmt != Stmt.Null;
    }
    public void setDefault(Stmt s){
        defaultStmt = s;
        stmts.stmt2 = new Seq(s,Stmt.Null);
        stmts = (Seq)stmts.stmt2;
    }

    public static Switch getSwitch(Expr c){
        if( c.type == Type.Str ){
            return new StrSwitch(c);
        } else if( c.type == Type.Char ) {
            return new CharSwitch(c);
        } else if( c.type == Type.Int ) {
            return new IntSwitch(c);
        } else {
            c.error("switch expression should be `" + Type.Int + "',`" + Type.Str + "' or `" + Type.Char + "',but `"+ c.type + "' found");
            return null;
        }
    }
}

class IntSwitch extends Switch {
        HashMap<Integer,Stmt> map = new HashMap<Integer,Stmt>();

        public IntSwitch(Expr e){
            super(e);
        }

        public boolean appendCase(Constant c,Stmt s){
            c = ConversionFactory.getConversion(c,Type.Int).getValue();
            int i = ((Num)c.op).value;
            stmts.stmt2 = new Seq(s,Stmt.Null);
            stmts = (Seq)stmts.stmt2;
            map.put(i,stmts);
            return false;
        }
        
        public boolean isCaseSet(Constant c){
            if( Type.max(Type.Int,c.type) != Type.Int){
                c.error("case type should be `" + Type.Int + 
                "',but `" + c.type + "' found.");
            }
            c = ConversionFactory.getConversion(c,Type.Int).getValue();
            int i = ((Num)c.op).value;
            return map.get(i) != null;
        }

        public void run(){
            Constant v = condition.getValue();
            int i = ((Num)v.op).value;
            Stmt s = map.get(i);
            try{
                if(s == null){
                    defaultStmt.run();
                } else {
                    s.run();
                }
            } catch(RuntimeException e){
                if(e.getCause() != Break.BreakCause)
                    throw e;
            }
        }
}

class CharSwitch extends Switch {
        public CharSwitch(Expr e){
            super(e);
        }
        
        public boolean appendCase(Constant c,Stmt s){
            return false;
        }
        
        public boolean isCaseSet(Constant c){
            return false;
        }
}

class StrSwitch extends Switch {
        public StrSwitch(Expr e){
            super(e);
        }
        
        public boolean appendCase(Constant c,Stmt s){
            return false;
        }
        
        public boolean isCaseSet(Constant c){
            return false;
        }
}
