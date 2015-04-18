package inter.stmt;

import lexer.*;
import symbols.*;
import inter.expr.*;

import java.util.Map.Entry;
import java.util.Iterator;
import java.util.HashMap;

public abstract class Switch extends Stmt {
    Expr condition;
    boolean isdefaultset = false;
    Stmt defaultStmt = Stmt.Null;
    Seq stmts = new Seq(Stmt.Null,Stmt.Null);
    Seq head  = stmts;
    protected int indexOfDefault = 0;

    public Switch(Expr c){
        condition = c;
    }

    public abstract boolean appendCase(Constant c,Stmt s);
    public abstract boolean isCaseSet(Constant c);
    public boolean isDefaultSet(){
        return isdefaultset;
    }

    public void setDefault(Stmt s){
        defaultStmt = s;
        push(s);
        isdefaultset = true;
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

    protected void run(Stmt s){
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

    protected Stmt push(Stmt s){
        if(stmts.stmt1 == Stmt.Null){
            stmts.stmt1 = s;
        } else {
            stmts.stmt2 = new Seq(s,Stmt.Null);
            stmts = (Seq)stmts.stmt2;
        }
        return stmts;
    }

    @Override
    public String toString(){
        return "switch(" + condition + ")\n";
    }
}

class IntSwitch extends Switch {
        HashMap<Integer,Stmt> map = new HashMap<Integer,Stmt>();

        public IntSwitch(Expr e){
            super(e);
        }

        @Override
        public boolean appendCase(Constant c,Stmt s){
            c = ConversionFactory.getConversion(c,Type.Int).getValue();
            int i = ((Num)c.op).value;
            map.put(i,push(s));
            if(!isdefaultset){
                indexOfDefault ++;
            }
            return false;
        }

        @Override
        public boolean isCaseSet(Constant c){
            if( Type.max(Type.Int,c.type) != Type.Int){
                c.error("case type should be `" + Type.Int + 
                "',but `" + c.type + "' found.");
            }
            c = ConversionFactory.getConversion(c,Type.Int).getValue();
            int i = ((Num)c.op).value;
            return map.get(i) != null;
        }

        @Override
        public void run(){
            Constant v = condition.getValue();
            int i = ((Num)v.op).value;
            Stmt s = map.get(i);
            super.run(s);
        }

}

class CharSwitch extends Switch {
        HashMap<Character,Stmt> map = new HashMap<Character,Stmt>();

        public CharSwitch(Expr e){
            super(e);
        }
        
        @Override
        public boolean appendCase(Constant c,Stmt s){
            c = ConversionFactory.getConversion(c,Type.Char).getValue();
            char i = ((Char)c.op).value;
            map.put(i,push(s));
            if(!isdefaultset){
                indexOfDefault ++;
            }
            return false;
        }
        
        @Override
        public boolean isCaseSet(Constant c){
            if( Type.max(Type.Int,c.type) != Type.Int){
                c.error("case type should be `" + Type.Char +
                "',but `" + c.type + "' found.");
            }
            if(Type.Int == c.type){
                int i = ((Num)c.op).value;
                if(i > Character.MAX_VALUE){
                    c.error("case value " + i +" great than the max value of `" + Type.Char + "' ");
                } else if(i < Character.MIN_VALUE){
                    c.error("case value " + i + " less than the min value of `" + Type.Char + "' ");
                }
            }

            c = ConversionFactory.getConversion(c,Type.Char).getValue();
            char i = ((Char)c.op).value;
            return map.get(i) != null;
        }

        @Override
        public void run(){
            Constant v = condition.getValue();
            char i = ((Char)v.op).value;
            Stmt s = map.get(i);
            super.run(s);
        }

}

class StrSwitch extends Switch {
        HashMap<String,Stmt> map = new HashMap<String,Stmt>();
        
        public StrSwitch(Expr e){
            super(e);
        }
        
        @Override
        public boolean appendCase(Constant c,Stmt s){
            String i = ((Str)c.op).value;
            map.put(i,push(s));
            if(!isdefaultset){
                indexOfDefault ++;
            }
            return false;
        }
        
        @Override
        public boolean isCaseSet(Constant c){
            if( c.type != Type.Str ){
                c.error("case type should be `" + Type.Str +
                "',but `" + c.type + "' found.");
            }
            String i = ((Str)c.op).value;
            return map.get(i) != null;
        }
        
        @Override
        public void run(){
            Constant v = condition.getValue();
            String i = ((Str)v.op).value;
            Stmt s = map.get(i);
            super.run(s);
        }

}
