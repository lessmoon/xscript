package inter.stmt;

import inter.util.Node;
import inter.expr.Constant;
import runtime.VarTable;

public class Stmt extends Node{
    public Stmt()
    {}

    public void run(){
        
    }

    public static final Stmt Null = new Stmt(){
        @Override
        public String toString() {
            return "NullStmt\n";
        }
    };

    public static final Stmt RecoverStack = new Stmt(){
        @Override
        public void run(){
            VarTable.popTop();
        }

        @Override
        public String toString(){
            return "RecoverStack\n";
        }
        /*
            void emitBinaryCode(BinaryCode x){
                x.emit(POP_STACK);
            }
        */
    };
    public static final Stmt PushStack = new Stmt(){
        @Override
        public void run(){
            VarTable.pushTop();
        }
        @Override
        public String toString(){
            return "PushStack\n";
        }
        /*
            void emitBinaryCode(BinaryCode x){
                x.emit(PUSH_STACK);
            }
        */
    };
    
    public static void ret(Constant c){
        throw new ReturnResult(c);
    }

    public static void ret(boolean b){
        ret(b?Constant.False:Constant.True);
    }

    @Override
    public String toString(){
        return this.getClass().getName() + "\n";
    }

    public Stmt optimize(){
        return this;
    }

	public boolean isLastStmt(){
		return false;
	}
	
    public static Stmt Enclosing        = Null;
    public static Stmt BreakEnclosing   = Null;

    public void appendToSeq(LinkedSeq s){
        s.appendStmt(this);
    }
}