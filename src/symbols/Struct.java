package symbols;

import lexer.*;
import inter.expr.Constant;
import inter.stmt.FunctionBasic;
import inter.stmt.MemberFunction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Struct extends Type {
    public final HashMap<Token,StructVariable> table = new HashMap<Token,StructVariable>();
    /*store normal functions*/
    final HashMap<Token,FunctionBasic> funcs = new HashMap<Token,FunctionBasic>();
    /*<k,v> => <operand,func_name>*/
    final HashMap<Token,Token> overload_funcs;
    /*store virtual functions*/
    final HashMap<Token,Position> vfunc_map;
    final VirtualTable vtable;
    boolean hasDefinedVirtualFunction = false;
    final Struct father;
    final int    father_size;/*avoid replicated calculating*/
    boolean has_used = false;
    int  first_used_line = -1;
    String first_used_file = "";
    
    public Struct(Token name){
        super(name.toString(),Tag.BASIC,Constant.Null);
        father = null;
        vtable = new VirtualTable();
        vfunc_map = new HashMap<Token,Position>();
        overload_funcs = new HashMap<Token,Token>();
        father_size = 0;
    }

    public Struct(Token name,Struct father){
        super(name.toString(),Tag.BASIC,Constant.Null);
        this.father = father;
        vtable = (VirtualTable)father.getVirtualTable().clone();
        vfunc_map = new HashMap<Token,Position>(father.vfunc_map);
        overload_funcs = new HashMap<Token,Token>(father.overload_funcs);
        father_size = father.getVariableNumber();
    }

    public boolean isCompleted(){
        return vtable.isCompleted();
    }

    public boolean isChildOf(Struct t){
        Struct f = this.father;
        while(f != null){
            if(t == f)
                return true;
            f = f.father;
        }
        return false;
    }

    @Override
    public boolean isBuiltInType(){
        return false;
    }

    @Override
    public boolean equals(Type t){
        return t == this ;
    }

    /*
     * get the virtual function position in the vtable
     * if it doesn't exist return null
     */
    public Position getVirtualFunctionPosition(Token vfname){
        return vfunc_map.get(vfname);
    }

    /*
     * return the type of the member named mname
     * return null if it doesn't exist
     */
    public StructVariable getMemberVariableType(Token mname){
        StructVariable t = table.get(mname);
        return (t != null || father==null)?t:father.getMemberVariableType(mname);
    }

    /*
     * Return not-null if it has conflict definition  
     * caller should handle those situations
     */
    public StructVariable addMemberVariable(Token mname,Type t){
        StructVariable x  ;
        if(father != null){
           x = father.getMemberVariableType(mname);
           if(x != null){
                return x;
           }
        }
        x = table.put(mname,new StructVariable(t,getVariableNumber()));
        return x;
    }

    public void defineVirtualFunction(Token name,FunctionBasic mf){
        if(!hasDefinedVirtualFunction){
            vtable.createNewTable();
            hasDefinedVirtualFunction = true;
        }
        FunctionBasic f = getFunction(name);
        if(f != null){
            mf.error("virtual function `" + mf + "' shadows a function `" + f + "'");
        }

        vtable.addVirtualFunction(mf);
        vfunc_map.put(name,new Position(vtable.getGenerations() - 1,vtable.getTopSize() - 1));
    }

    public FunctionBasic getVirtualFunction(Token name){
        Position p = vfunc_map.get(name);
        return p==null?null:vtable.getVirtualFunction(p.generation,p.index);
    }

    /*
     * override a virtual function
     * if it doesn't existed throw error
     */
    public void overrideVirtualFunction(Token name,FunctionBasic mf){
        Position p = vfunc_map.get(name);
        /*it is not a virtual function of base */
        if(p == null || p.generation > vtable.getGenerations() - 1){
            mf.error("override error:virtual function definition `"  + this.lexeme + "." + name + "' not found");
        }
        FunctionBasic f = vtable.getVirtualFunction(p);
        assert(f != null);
        if(f.getParaNumber() != mf.getParaNumber()){
            f.error("virtual function `" + this.lexeme + "." + f.name + "':parameters number should be "+ f.getParaNumber() + ",but found " + f.getParaNumber());
        }
        if(!f.type.equals(mf.type)){
            f.error("virtual function `" + this.lexeme + "." + f.name + "':return type should be `" + mf.type + "',but found `" + f.type +"'");
        }
        for(int i = 1; i < f.getParaNumber() ;i++ ){
            if(!f.getParaInfo(i).type.equals(mf.getParaInfo(i).type)){
                mf.error("virtual function `" + this.lexeme + "." + mf.name + "':parameter [" + i + "]" + " type is `" +  mf.getParaInfo(i).type + "',but found `" +  f.getParaInfo(i).type + "'");
            }
        }
        vtable.overrideVirtualFunction(p.generation,p.index,mf);
    }

    public FunctionBasic getDeclaredFunction(Token name){
        MemberFunction f = (MemberFunction)getVirtualFunction(name);
        if(f == null){
            f = (MemberFunction)getNormalFunction(name);
        }
        /*
         * The function of this struct doesn't exist
         * Maybe useful when predefine a function
         */
        if(f.getStruct() != this){
            return null;
        }
        return f;
    }
    
    /*
     * get base function
     */
    public FunctionBasic getFunction(Token name){
        FunctionBasic f = getVirtualFunction(name);
        if(f == null){
            f = getNormalFunction(name);
        }
        return f;
    }

    /*
     * Set operand overloading by operand
     * return true if it is not redefined
     */
    public boolean addOverloading(Token op,FunctionBasic f){
        /*
         * should operand be inheritable?
         */
        switch(op.tag){
        //arith operand
        case '+':case '-':case '*':case '/':case '%':
            /*parameter is at least two(this point,and another struct)*/
            if(f.paralist.size() != 2){
                f.error("Operand `" + op  + "' overloading function should just use one parameters,but found `" + f.paralist.size() + "'");
            }
            /*should be the same as the struct*/
            if(!f.paralist.get(1).type.equals(this)){
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
            if(op == this ){
                f.error("Type-conversion overloading can't be used for self conversion\nnote:" + this + "->" + this);
            } else if(op instanceof Struct && this.isChildOf((Struct)op)){
                f.error("Type-conversion overloading can't be used for base type conversion:\nnote:" + this + "->" + op);
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
        return overload_funcs.put(op,f.name) == null;
    }

    /*
     * Get operand overloading by operand
     */
    public Token getOverloading(Token op){
        return overload_funcs.get(op);
    }

    /*
     * Just add normal function
     */
    public FunctionBasic addNormalFunction(Token fname,FunctionBasic f){
        Position p = vfunc_map.get(fname);
        FunctionBasic f2;
        f2 = getFunction(fname);
        if(f2 != null){
           f.error("function `"  + this.lexeme + "." + f + "' has conflict name with a function `" + f2 +"'"); 
        }
        f2 = funcs.put(fname,f);
        assert(f2 == null);
        return null;
    }

    public FunctionBasic getNormalFunction(Token fname){
        FunctionBasic f = funcs.get(fname);
        return (f != null || father == null)?f:father.getNormalFunction(fname);
    }

    public VirtualTable getVirtualTable(){
        return vtable;
    }

    public Struct getFather(){
        return father;
    }

    public int getVariableNumber(){
        return father_size + table.size();
    }

    public void setUsed(int ful,String fuf){
        if(!has_used){
            has_used = true;
            first_used_file = fuf;
            first_used_line = ful;
        }
    }
    
    public boolean used(){
        return has_used;
    }
    
    public int getFirstUsedLine(){
        return first_used_line;
    }
    
    public String getFirstUsedFile(){
        return first_used_file;
    }
    
    @Override
    public String toString(){
        return  "struct " + super.toString();
    }

}