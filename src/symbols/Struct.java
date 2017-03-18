package symbols;

import inter.expr.Value;
import inter.stmt.FunctionBasic;
import inter.stmt.MemberFunction;
import lexer.Tag;
import lexer.Token;
import lexer.Word;

import java.util.*;
import java.util.stream.Stream;

public class Struct extends Type implements Iterable<StructVariable>{
    public static final Struct StructPlaceHolder  = new Struct(new Word("#StructPlaceHolder#",Tag.ID));
    private final Map<Token,StructVariable> variableMap = new HashMap<>();
    /*store normal functions*/
    private final HashMap<Token,FunctionBasic> functionMap = new HashMap<>();
    /*<k,v> => <operand,func_name>*/
    private final Token name;
    private final Map<Token,Token> overloadFunctions;
    /*store virtual functions*/
    private final Map<Token,Position> virtualFunctionPositionMap;
    private final VirtualTable virtualTable;
    private FunctionBasic initFunction = null;
    private boolean hasDefinedVirtualFunction = false;
    private final Struct baseStruct;
    private int baseSize;/*avoid replicated calculating*/
    private boolean instantiated = false;
    private int firstInstantiatedLine = -1;
    private Set<Struct> derivedStructSet = new HashSet<>();

    private String firstInstantiatedFile = "";
    private boolean closed = false;
    private boolean needCopyBase;
    private  Value[] instanceMemoryTemplate;
    private int firstInstantiatedIndex;

    public Struct(Token name){
        super(name.toString(),Tag.BASIC, Value.Null);
        this.name = name;
        baseStruct = null;
        virtualTable = new VirtualTable();
        virtualFunctionPositionMap = new HashMap<>();
        overloadFunctions = new HashMap<>();
        baseSize = 0;
        needCopyBase = false;
    }

    public Struct(Token name,Struct baseStruct){
        super(name.toString(),Tag.BASIC, Value.Null);
        this.name = name;
        this.baseStruct = baseStruct;
        baseStruct.addDerivedStruct(this);
        needCopyBase = !baseStruct.isClosed();
        virtualTable = (VirtualTable) baseStruct.getVirtualTable().clone();
        virtualFunctionPositionMap = new HashMap<>(baseStruct.virtualFunctionPositionMap);
        overloadFunctions = new HashMap<>(baseStruct.overloadFunctions);
        baseSize = baseStruct.countVariables();
    }

    public void copyBase(){
        if(needCopyBase) {
            virtualTable.copy(baseStruct.getVirtualTable());
            virtualFunctionPositionMap.clear();
            virtualFunctionPositionMap.putAll(baseStruct.virtualFunctionPositionMap);
            overloadFunctions.clear();
            overloadFunctions.putAll(baseStruct.overloadFunctions);
            baseSize = baseStruct.countVariables();
            needCopyBase = false;
        }
    }

    public Struct close() {
        if ( !closed  ) {
            if (baseStruct != null && !baseStruct.isClosed()) {
                throw new RuntimeException("base struct `" + baseStruct + "' of `" + this + "' is not closed yet");
            }
            copyBase();
            closed = true;
        }
        return this;
    }

    public void addDerivedStruct(Struct s){
        assert s != null&&s.getBaseStruct() == this;
        this.derivedStructSet.add(s);
    }

    public Iterator<Struct> iteratorDerivedStruct(){
        return this.derivedStructSet.iterator();
    }

    /**
     * Create memory space for new instance of this struct,each variable has been initialized<br/>
     * The memory map is arranged order by the defined position as following:<br/>
     *       _________________________<br/>
     *      |     Base Struct Map    |<br/>
     *      |________________________|<br/>
     *      |  1st defined variable  |<br/>
     *      |________________________|<br/>
     *      |  nth defined variable  |<br/>
     *      |________________________|
     * @return the instance memory created
     */
    public Value[] createInstanceMemory(){
        if(instanceMemoryTemplate == null){
            instanceMemoryTemplate = new Value[countVariables()];
            Struct t = this;
            do{
                t.forEach(i -> instanceMemoryTemplate[i.index] = i.type.getInitialValue());
            }while((t = t.getBaseStruct()) != null);
        }
        return instanceMemoryTemplate.clone();
    }
    /**
     * A struct is closed iff:<br/>
     *   1.Its base struct is closed<br/>
     *   2.All of its variables and its functions has been DECLARED(not DEFINED).
     */
    public boolean isClosed() {
        return this.closed;
    }

    /**
     * Check if this struct still need copy base struct's variables table's information<br/>
     *  Because the base struct may not be closed {@see #isClosed} when this struct is pre-declared
     * @return if this struct need copy base
     */
    public boolean needCopyBase(){
        return needCopyBase;
    }

    /**
     * @return a constant reference of the VariableMap.values()
     */
    public Collection<StructVariable> getStructVariables(){
        return Collections.unmodifiableCollection(variableMap.values());
    }

    /**
     * A struct is completed iff<br/>
     *  1.it has no initial function or its initial function is defined already<br/>
     *  2. it has no virtual functions or its all virtual function has been defined
     * @return if the struct is completed
     */
    public boolean isCompleted(){
        return (initFunction == null || initFunction.isCompleted())&& virtualTable.isCompleted();
    }

    /**
     * Struct D is child of struct B iff<br/>
     *      1.D inherits B directly<br/>
     *      2.Base struct of D is child of struct B
     * @param struct the struct type to compare with
     * @return true if the struct is a child struct of {@code struct}
     */
    public boolean isChildOf(Struct struct){
        Struct f = this.baseStruct;
        while(f != null){
            if(struct == f)
                return true;
            f = f.baseStruct;
        }
        return false;
    }

    public final Token getName(){
        return name;
    }

    @Override
    public boolean isBuiltInType(){
        return false;
    }

    public FunctionBasic getInitialFunction(){
        return initFunction;
    }

    /**
     * Define the struct initial function
     * @param f the initial functions
     */
    public void defineInitialFunction(FunctionBasic f){
        if(initFunction != null){
            f.error("initial function of `" + this + "' has been defined");
        }
        initFunction = f;
    }
    
    /**
     * @return the virtual function position in the virtualTable or null if it doesn't exist
     */
    public Position getVirtualFunctionPosition(Token name){
        return virtualFunctionPositionMap.get(name);
    }

    /**
     * @param name the variable name
     * @return the type of the member named name or null if it doesn't exist
     */
    public StructVariable getVariable(Token name){
        StructVariable t = variableMap.get(name);
        return (t != null || baseStruct ==null)?t: baseStruct.getVariable(name);
    }

    /**
     * Add a member variable,if the name is occupied return previous variable or null
     * @param name  the variable name
     * @param type the member variable type
     * @return previous member variable if it has conflict definition
     */
    public StructVariable addVariable(Token name, Type type){
        StructVariable x  ;
        if(baseStruct != null){
           x = baseStruct.getVariable(name);
           if(x != null){
                return x;
           }
        }
        return variableMap.put(name,new StructVariable(type, countVariables()));
    }

    /**
     * Define a virtual function.This function will handle conflicts when it shadows a naive function({@link #addNaiveFunction(Token, FunctionBasic)})
     * @param name the virtual function's name
     * @param mf the member function body
     * @return the position of the virtual function
     */
    public Position defineVirtualFunction(Token name, FunctionBasic mf) {
        if(!hasDefinedVirtualFunction){
            virtualTable.createNewTable();
            hasDefinedVirtualFunction = true;
        }
        FunctionBasic f = getFunction(name);
        if(f != null){
            mf.error("virtual function `" + mf + "' shadows a function `" + f + "'");
        }

        virtualTable.addVirtualFunction(mf);
        final Position p = new Position(virtualTable.getGenerations() - 1, virtualTable.getTopSize() - 1);
        virtualFunctionPositionMap.put(name, p);
        return p;
    }

    public FunctionBasic getVirtualFunction(Token name){
        Position p = virtualFunctionPositionMap.get(name);
        return p==null?null: virtualTable.getVirtualFunction(p.generation,p.index);
    }

    /**
     * Declare a virtual function overriding,it will handle most errors in following situation:<br/>
     *  1.if not found the virtual function declaration<br/>
     *  2.if it is replicated declaration<br/>
     *  3.if its signature is not the same with declaration(parameters,return type)
     * @param name the function name
     * @param mf then function body
     */
    public void overrideVirtualFunction(Token name,FunctionBasic mf){
        Position p = virtualFunctionPositionMap.get(name);
        /*it is not a virtual function of base */
        if(p == null || p.generation > virtualTable.getGenerations() - 1){
            mf.error("override error:virtual function declaration `"  + this.lexeme + "." + name + "' not found");
            return;//avoid warning
        }
        FunctionBasic f = virtualTable.getVirtualFunction(p);
        assert(f != null);
        /*
         * Redeclared!
         */
        if(f != baseStruct.getVirtualTable().getVirtualFunction(p)){
            mf.error("virtual function `" + this.lexeme + "." + f.getName() + "':overriding has been declared before");
        }
        if(f.getParamSize() != mf.getParamSize()){
            mf.error("virtual function `" + this.lexeme + "." + f.getName() + "':parameters number should be "+ f.getParamSize() + ",but found " + mf.getParamSize());
        }
        if(!f.getType().isCongruentWith(mf.getType())){
            mf.error("virtual function `" + this.lexeme + "." + f.getName() + "':return type should be `" + f.getType() + "',but found `" + mf.getType() +"'");
        }
        for(int i = 1; i < f.getParamSize() ; i++ ){
            if(!f.getParamInfo(i).type.isCongruentWith(mf.getParamInfo(i).type)){
                mf.error("virtual function `" + this.lexeme + "." + f.getName() + "':parameter [" + i + "]" + " type is `" +  f.getParamInfo(i).type + "',but found `" +  mf.getParamInfo(i).type + "'");
            }
        }
        virtualTable.overrideVirtualFunction(p.generation,p.index,mf);
    }

    /**
     * get a function by name(search in both naive and virtual functions declared in this struct definition body)
     * @param name the function name
     * @return the function or null if it doesn't exist
     */
    public FunctionBasic getDeclaredFunction(Token name){
        FunctionBasic f = getFunction(name);
        /*
         * The function of this struct doesn't exist
         * Maybe useful when predefine a function
         */
        return (f == null || ((MemberFunction) f).getStruct() != this) ? null : f;
    }

    /**
     * Get a function by name
     * @param name the function name
     * @return the functions
     */
    private FunctionBasic getFunction(Token name){
        FunctionBasic f = getVirtualFunction(name);
        return f != null?f:getNaiveFunction(name);
    }

    /**
     * Set operand overloading by op<br/>
     * Handled most of errors
     * @param op the operand name
     * @param f the function bind to this op
     * @return true if it is not redefined or false
     */
    public boolean addOverloading(Token op,FunctionBasic f){
        /*
         * should operand be inheritable?
         */
        switch(op.tag){
        //arith operand
        case '+':case '-':case '*':case '/':case '%':
            /*parameter is at least two(this point,and another struct)*/
            if(f.getParamList().size() != 2){
                f.error("Operand `" + op  + "' overloading function should just use one parameters,but found `" + f.getParamList().size() + "'");
            }
            /*should be the same as the struct*/
            if(!f.getParamList().get(1).type.isCongruentWith(this)){
                f.error("Operand `" + op  + "' overloading function's parameter should be `" + this + "',but found `" + f.getParamList().get(1).type + "'");
            }
            /*return type should be the same as the struct*/
            if(f.getType() != this){
                f.error("Operand `" + op + "' overloading' return type should be `"+ this + "',but found `" + f.getType() + "'");
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
            if(f.getParamList().size() != 2){
                f.error("Operand `" + op  + "' overloading function should just use one parameters,but found `" + f.getParamList().size() + "'");
            }
            /*should be the same as the struct*/
            if(f.getParamList().get(1).type != this){
                f.error("Operand `" + op  + "' overloading function's parameter should be `" + this + "',but found `" + f.getParamList().get(1).type + "'");
            }
            /*return type should be the same as the struct*/
            if(f.getType() != Type.Bool){
                f.error("Operand `" + op + "' overloading' return type should be `"+ Type.Bool + "',but found `" + f.getType() + "'");
            }
            break;
        case Tag.BASIC:
            if(op == this ){
                f.error("Type-conversion overloading can't be used for self conversion\nnote:" + this + "->" + this);
            } else if(op instanceof Struct && this.isChildOf((Struct)op)){
                f.error("Type-conversion overloading can't be used for base type conversion:\nnote:" + this + "->" + op);
            }

            if(f.getParamList().size() != 1){
                f.error("Type-conversion overloading operand `" + op + "' shouldn't have any parameter");
            }
            /*return type should be the same as the op*/
            if(!f.getType().isCongruentWith((Type)op)){
                f.error("Type-conversion overloading(`" + op + "') function should return the same type,but found `" + f.getType() + "'");
            }
            
            break;
        default:
            f.error("Can't overload operand `" + op + "'");
            break;
        }
        return overloadFunctions.put(op, f.getName()) == null;
    }

    /**
     * Get operand overloading by operand
     * @param op the overloading name
     * @return the function bind to the operand
     */
    public Token getOverloading(Token op){
        return overloadFunctions.get(op);
    }

    /**
     * Add naive function which means it is not virtual
     * @throws RuntimeException when the {@param function} has conflicts with others
     * @param functionName the function name
     * @param function  the function body
     * @return null if it has no conflicts
     */
    public FunctionBasic addNaiveFunction(Token functionName, FunctionBasic function){
        FunctionBasic f2 = getFunction(functionName);
        if(f2 != null){
           function.error("function `" +  function + "' has a conflict name with a function `" + f2 +"'");
        }
        f2 = functionMap.put(functionName,function);
        assert(f2 == null);
        return null;
    }

    /**
     * Get naive function by name
     * @param functionName the function name
     * @return the function
     */
    public FunctionBasic getNaiveFunction(Token functionName){
        FunctionBasic f = functionMap.get(functionName);
        return (f != null || baseStruct == null)?f: baseStruct.getNaiveFunction(functionName);
    }

    public VirtualTable getVirtualTable(){
        return virtualTable;
    }

    public Struct getBaseStruct(){
        return baseStruct;
    }

    /**
     * Count all variable include variables defined in this struct
     * @return number of all member variables
     */
    public int countVariables(){
        return baseSize + variableMap.size();
    }

    /**
     * Record where(file and line) the struct is first instantiated
     * @param fil the line number
     * @param fii the line offset
     * @param fif the file name
     */
    public void setInstantiated(int fil, int fii, String fif){
        if(!instantiated){
            instantiated = true;
            firstInstantiatedFile = fif;
            firstInstantiatedLine = fil;
            firstInstantiatedIndex = fii;
        }
    }

    public boolean isInstantiated(){
        return instantiated;
    }
    
    public int getFirstInstantiatedLine(){
        return firstInstantiatedLine;
    }

    public int getFirstInstantiatedIndex() {
        return firstInstantiatedIndex;
    }

    public String getFirstInstantiatedFile(){
        return firstInstantiatedFile;
    }

    public String getDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("struct ").append(name).append(baseStruct == null ? "" : " : " + baseStruct.name).append("{\n");
        if (!variableMap.isEmpty()) {
            stringBuilder.append("    //variables\n");
            variableMap.entrySet().stream()
                    .sorted(Comparator.comparingInt(a -> a.getValue().index))
                    .forEach(sv -> stringBuilder.append("    ").append(sv.getValue().type).append(" ").append(sv.getKey()).append(";\n"));
        }

        if(initFunction != null){
            stringBuilder.append("    //setBody function\n");
            stringBuilder.append("    def ").append(initFunction.getDescription(false)).append(";\n");
        }
        if (!functionMap.isEmpty()) {
            stringBuilder.append("    //functions\n");
            functionMap.entrySet()
                    .forEach(sv -> stringBuilder.append("    def ").append(sv.getValue().getDescription(false)).append(";\n"));
        }
        if (virtualTable.getGenerations() > 0) {
            Stream<Position> positionStream = virtualFunctionPositionMap.values().stream().filter(pos -> virtualTable.getVirtualFunction(pos).isCompleted());
            if (positionStream.count() > 0) {
                positionStream = virtualFunctionPositionMap.values().stream().filter(pos -> virtualTable.getVirtualFunction(pos).isCompleted());
                stringBuilder.append("    //virtual functions\n");
                positionStream.forEach(pos -> stringBuilder.append("    def virtual ").append(virtualTable.getVirtualFunction(pos).getDescription(false)).append(";\n"));
            }
        }
        if (virtualTable.getGenerations() > 0) {
            Stream<Position> positionStream = virtualFunctionPositionMap.values().stream().filter(pos -> !virtualTable.getVirtualFunction(pos).isCompleted());
            if (positionStream.count() > 0) {
                positionStream = virtualFunctionPositionMap.values().stream().filter(pos -> !virtualTable.getVirtualFunction(pos).isCompleted());
                stringBuilder.append("    //pure virtual functions\n");
                positionStream.forEach(pos -> stringBuilder.append("    def virtual ").append(virtualTable.getVirtualFunction(pos).getDescription(false)).append(";\n"));
            }
        }

        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * Iterator of the variables
     * @return struct variable iterator
     */
    @Override
    public Iterator<StructVariable> iterator() {
        return getStructVariables().iterator();
    }


}