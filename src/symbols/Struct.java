package symbols;

import lexer.*;
import inter.stmt.FunctionBasic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Struct extends Type {
    public HashMap<Token,Type> table = new HashMap<Token,Type>();
    HashMap<Token,FunctionBasic> funcs = new HashMap<Token,FunctionBasic>();
    HashMap<Token,FunctionBasic> override_funcs = new HashMap<Token,FunctionBasic>();
    
    public Struct(Token name){
        super(name.toString(),Tag.BASIC);
        //assert(!t.empty());
    }

    public boolean equals(Type t){
        return t == this;
    }

    public Type addEntry(Token mname,Type t){
        return table.put(mname,t);
    }

    /*
     * Set operand overloading by operand
     * return true if it is not redefined
     */
    public boolean addOverloading(Token op,FunctionBasic f){
        switch(op.tag){
        //arith operand
        case '+':
        case '-':
        case '*':
        case '/':
        case '%':
            /*parameter is at least two(this point,and another struct)*/
            if(f.paralist.size() != 2){
                f.error("Operand `" + op  + "' overloading function should just use one parameters,but found `" + f.paralist.size() + "'");
            }
            /*should be the same as the struct*/
            if(f.paralist.get(1).type != this){
                f.error("Operand `" + op  + "' overloading function's parameter should be `" + this + "',but found `" + f.paralist.get(1).type + "'");
            }
            /*return type should be the same as the struct*/
            if(f.type != this){
                f.error("Operand `" + op + "' overloading' return type should be `"+ this + "',but found `" + f.type + "'");
            }
            break;
        
        /*
         * NOTE:
         * recursively calling risk:
         * it will call itself while do this(maybe you don't know)
         * so we just disable =='s and !='s overloading
         * e.g.:
         *   @== or(!=)
         *   def bool isequal(list s){
         *      if(s == this){//at here
         *          return true;
         *      }
         *      else
         *          ......
         *   }
         */
        //relational operand
        //case Tag.NE://!=
        //case Tag.EQ://==
        case Tag.GE://>=
        case Tag.LE://<=
        case '<':
        case '>':
            /*parameter is at least two(this point,and another struct)*/
            if(f.paralist.size() != 2){
                f.error("Operand `" + op  + "' overloading function should just use one parameters,but found `" + f.paralist.size() + "'");
            }
            /*should be the same as the struct*/
            if(f.paralist.get(1).type != this){
                f.error("Operand `" + op  + "' overloading function's parameter should be `" + this + "',but found `" + f.paralist.get(1).type + "'");
            }
            /*return type should be the same as the struct*/
            if(f.type != Type.Bool){
                f.error("Operand `" + op + "' overloading' return type should be `"+ Type.Bool + "',but found `" + f.type + "'");
            }
            break;
        case Tag.BASIC:
            if(op == this){
                f.error("Type-conversion overloading can't be used for self conversion");
            }
            if(f.paralist.size() != 1){
                f.error("Type-conversion overloading operand `" + op + "' shouldn't have any parameter");
            }
            /*return type should be the same as the op*/
            if(!f.type.equals((Type)op)){
                f.error("Type-conversion overloading(`" + op + "') function should return the same type,but found `" + f.type + "'");
            }
            
            break;
        default:
            f.error("Can't overload operand `" + op + "'");
            break;
        }
        return override_funcs.put(op,f) == null;
    }

    /*
     * Get operand overloading by operand
     */
    public FunctionBasic getOverloading(Token op){
        return override_funcs.get(op);
    }
    /*
     * return the type of the member named mname
     * return null if it doesn't exist
     */
    public Type getType(Token mname){
        return table.get(mname);
    }

    public FunctionBasic addFunc(Token fname,FunctionBasic f){
        return funcs.put(fname,f);
    }

    public FunctionBasic getFunc(Token fname){
        return funcs.get(fname);
    }

    @Override
    public String toString(){
        return  "struct " + super.toString();
    }

}