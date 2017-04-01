package parser;

import inter.expr.*;
import inter.stmt.*;
import inter.util.Node;
import inter.util.Param;
import lexer.*;
import runtime.LoadFunc;
import runtime.LoadStruct;
import runtime.TypeTable;
import symbols.*;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.Set;

public class Parser implements TypeTable {
    private final boolean ENABLE_EXPR_OPT;
    private final boolean ENABLE_STMT_OPT;
    private Lexer lex;
    private Token look;
    private Env top = new Env();
    private Map<Token, Type> typeTable = new LinkedHashMap<>();
    private FuncTable table = new FuncTable();
    private Set<Token> forbiddenFunctionName = new HashSet<>();
    private Type returnType = Type.Int;
    private boolean hasDecl = true;
    final private boolean enableWarning = false;
    private boolean PRINT_STRUCT = false;
    /*
     * binding for super.func or var,
     * which means super struct must have this function definition and implementation
     * this call of this function must be a normal call(directly invoking)
     */
    private boolean isSuperCalled = false;
    private Struct dStruct = null;
    private boolean isInStructInitialFunctionDefinition = false;
    /*integer numbers standing for the stack level*/
    private int lastBreakFatherLevel = -1;
    private int lastIterationLevel = -1;
    /*
     * The variable lastFunctionLevel is for the
     * local function definition.
     * For now,it is just 0(which means global level).
     */
    private int lastFunctionLevel = 0;
    private int nowLevel = 0;
    private Set<FunctionBasic> fUsed = new HashSet<>();
    private boolean PRINT_FUNC_TRANSLATE = false;
    private Set<Token> capturedVars = new HashSet<>();
    private int anonymousInnerStructId = 0;

    public Parser(Lexer l) throws IOException {
        this(l, false, false);
    }

    public Parser(Lexer l, boolean expr_opt, boolean stmt_opt) throws IOException {
        lex = l;
        move();
        top.put(Word.args, new Array(Type.Str));
        ENABLE_EXPR_OPT = expr_opt;
        ENABLE_STMT_OPT = stmt_opt;
        predefinedForbiddenFunctionName();
        reserveBasicTypes();
    }

    private void move() throws IOException {
        look = lex.scan();
    }

    private void predefinedForbiddenFunctionName() {
        forbiddenFunctionName.add(Word.This);
        forbiddenFunctionName.add(Word.Super);
    }

    private void reserveBasicTypes() {
        defType(Type.BigInt, Type.BigInt);
        defType(Type.BigReal, Type.BigReal);
        defType(Type.Bool, Type.Bool);
        defType(Type.Char, Type.Char);
        defType(Type.Int, Type.Int);
        defType(Type.Str, Type.Str);
        defType(Type.Real, Type.Real);
        defType(Type.Void, Type.Void);
        defType(Word.Auto, Type.Auto);
    }

    private Type defType(Token tok, Type t) {
        return typeTable.put(tok, t);
    }

    public static void main(String[] args) throws Exception {
        Lexer l = new Lexer();
        l.open("test.txt");
        new Parser(l).program().run();
    }

    private boolean isForbiddenFunctionName(Token name) {
        return forbiddenFunctionName.contains(name);
    }

    public void enablePrintFuncTranslate() {
        PRINT_FUNC_TRANSLATE = true;
    }

    public void enablePrintStruct() {
        PRINT_STRUCT = true;
    }

    public void disablePrintStruct() {
        PRINT_STRUCT = false;
    }

    public void disablePrintFuncTranslate() {
        PRINT_FUNC_TRANSLATE = false;
    }

    private void checkNamespace(Token name, String info) {
        if (top.get(name) != null) {
            error(info + " `" + name + "' has a same name with a variable");
        }

        if (getAutoType(name) != null) {
            error(info + " `" + name + "' has a same name with a struct");
        }
    }

    private Token copymove() throws IOException {
        Token tmp = look;
        move();
        return tmp;
    }

    public void error(String s) throws RuntimeException {
        error(s, null);
    }

    public void error(String s, Throwable cause) {
        error(s, Lexer.line, Lexer.offset, Lexer.filename, cause);
    }

    public void error(String s, int l, int offset, String f) {
        error(s, l, offset, f, null);
    }

    public void error(String s, int l, int offset, String f, Throwable e) throws RuntimeException {
        if (e == null)
            throw new RuntimeException(" in file `" + f + "' at line " + l + ":" + offset + ":\n\t" + s);
        else
            throw new RuntimeException(" in file `" + f + "' at line " + l + ":" + offset + ":\n\t" + s, e);
    }

    public void warning(String s) {
        warning(s, Lexer.line, Lexer.filename);
    }

    private void warning(String s, int l, String f) {
        if (enableWarning)
            System.err.println("line " + l + " in file `" + f + "':\n\t" + s);
    }

    private void match(char t) throws IOException {
        if (look.tag == t)
            move();
        else {
            if (look.tag == -1) {
                error("unexpected end of file");
            }
            error("syntax error:expect `" + t + "',but found `" + look + "'");
        }
    }

    private void match(int t) throws IOException {
        if (look.tag == t)
            move();
        else {
            if (look.tag == -1) {
                error("unexpected end of file");
            }
            String expect;
            switch (t) {
                case Tag.ID:
                    expect = "identifier";
                    break;
                case Tag.BASIC:
                    expect = "basic type(includes (big)int,(big)real,char,bool and string)";
                    break;
                case Tag.STR:
                    expect = "string constant";
                    break;
                default:
                    expect = "";
            }
            error("syntax error: expect " + expect + ",but found `" + look + "'");
        }
    }

    public boolean check(int t) throws IOException {
        if (look.tag == t) {
            move();
            return true;
        } else {
            return false;
        }
    }

    public Stmt program() throws IOException {
        LinkedSeq s = new LinkedSeq();
        while (look.tag != -1) {
            switch (look.tag) {
                case Tag.DEF:
                    defFunction();
                    break;
                case Tag.STRUCT:
                    defStruct();
                    break;
                case Tag.NATIVE:
                    loadNative();
                    break;
                case Tag.IMPORT:
                    importLib();
                    break;
                default:
                    s.append(stmt());
                    break;
            }
        }

        checkFunctionCompletion();
        if (PRINT_STRUCT) {
            typeTable.forEach((id, t) -> {
                if (t instanceof Struct) {
                    System.out.println(((Struct) t).getDescription());
                }
            });
        }

        return ENABLE_STMT_OPT ? s.optimize() : s;
    }

    private void checkFunctionCompletion() {
        /*check if some used functions has not been completed*/

        table.getAllFunctions().stream()
                .filter(FunctionBasic::used)
                .filter(FunctionBasic::isNotCompleted)
                .forEach(b -> error("function `" + b + "' used but not completed ", b.line, b.offset, b.filename));

        fUsed.stream()
                .filter(FunctionBasic::used)
                .filter(FunctionBasic::isNotCompleted)
                .forEach(b -> error("function `" + b + "' used but not completed ", b.line, b.offset, b.filename));

        /*
         * check if there is a struct that is pure virtual but declared
         */
        for (Entry<Token, Type> info : typeTable.entrySet()) {
            if (!(info.getValue() instanceof Struct)) {
                continue;
            }
            Struct st = (Struct) (info.getValue());
            FunctionBasic f;
            if (st.isInstantiated() && (f = st.getFirstUncompletedFunction()) != null) {
                error("`" + info.getValue() + "' instantiated but not completed:" + f, st.getFirstInstantiatedLine(), st.getFirstInstantiatedIndex(), st.getFirstInstantiatedFile());
            }
        }
    }

    private void importLib() throws IOException {
        match(Tag.IMPORT);
        Token l = look;
        match(Tag.STR);
        if (look.tag != ';') {
            error("syntax error: expect `;',but found `" + look + "'");
        }
        lex.open(((Str) l).value);
        /*
         * ignore the `;'
         */
        move();
    }

    private void checkExtensionDefinintion(Struct s) throws IOException {
        Set<Token> defined = new HashSet<>();
        while (!check('}')) {
            if (check(Tag.DEF)) {//inner function declaration;
                FunctionBasic f;
                if (look == Word.This) {//initial function
                    if (defined.contains(look)) {
                        error("found redeclared initial function of " + s);
                    }
                    defined.add(look);
                    f = s.getInitialFunction();
                    if (f == null) {
                        error("struct `" + s + "' has no initial function");
                        return;
                    }
                    move();
                } else {
                    boolean isVirtual = (check(Tag.OVERRIDE) || check(Tag.VIRTUAL));
                    Type returnType = type();
                    Token fname = look;
                    if (defined.contains(fname)) {
                        error("found redeclared function " + s + "." + fname);
                    }
                    defined.add(fname);
                    match(Tag.ID);
                    f = isVirtual ? s.getVirtualFunction(fname) : s.getNaiveFunction(fname);
                    if (f == null) {
                        error("`" + s + "' has no" + (isVirtual ? " virtual " : " ") + "function `" + fname + "'");
                        return;
                    }
                    if (!returnType.isCongruentWith(f.getType())) {
                        error("function `" + s + "." + f.getName() + "' return type not match[expect `" + f.getType() + "', but found `" + returnType + "']");
                    }

                }
                Env savedTop = top;
                top = new Env(top);
                List<Param> parameters = parameters();
                top = savedTop;
                match(';');

                //this
                if (parameters.size() + 1 != f.getParamSize()) {
                    error("declared function " + f + " parameters number not matched[expect:`" + (f.getParamSize() - 1) + "',but found `" + parameters.size() + "']");
                }

                for (int i = 0; i < parameters.size(); i++) {
                    if (!parameters.get(i).type.isCongruentWith(f.getParamInfo(i + 1).type)) {
                        error("function " + f + " parameter " + parameters.get(i).type + "[" + i + "] type not matched with the declared one[expect:`" + f.getParamInfo(i + 1).type + "',but found:`" + parameters.get(i).type + "']");
                    }
                }
            } else {
                Type t = type();
                Token varName = look;
                match(Tag.ID);
                match(';');
                if (defined.contains(varName)) {
                    error("member " + s + "." + varName + " redeclared");
                }
                defined.add(varName);
                StructVariable t1 = s.getVariable(varName);
                if (t1 == null) {
                    error("member " + s + "." + varName + " does not actually exists");
                    return;//forbidden warning
                }
                if (!t1.type.isCongruentWith(t)) {
                    error("type of member " + s + "." + varName + "does not match actually[expect:`" + t1 + "',but found:`" + t + "']");
                }
            }
        }
    }

    private void loadNative() throws IOException {
        List<Param> pl;
        match(Tag.NATIVE);
        StringBuilder sb = new StringBuilder();
        match('<');
        Token pkg = look;
        match(Tag.ID);
        sb.append(pkg.toString());
        while (look.tag == '.' || look.tag == '$') {
            sb.append((char) look.tag);
            move();
            pkg = look;
            match(Tag.ID);
            sb.append(pkg.toString());
        }
        match('>');
        match('{');
        while (!check('}')) {
            String clazzName = null;
            if (look.tag == Tag.STR) {
                clazzName = look.toString();
                move();
                match(':');
            }

            if (check(Tag.STRUCT)) {
                /*
                 * load struct
                 */
                Token name = look;
                match(Tag.ID);
                if (clazzName == null) {
                    clazzName = name.toString();
                }

                Struct s;
                try {
                    s = LoadStruct.loadStruct(sb.toString(), clazzName, name, this.lex, this);
                } catch (Exception e) {
                    error("failed to load extension struct `" + sb.toString() + "." + clazzName + "'", e);
                    return;
                }
                checkNamespace(s.getName(), "struct");
                defType(s.getName(), s);

                if (check(':')) {
                    Token base = look;
                    match(Tag.ID);
                    Type b = getType(base);
                    if (b == null) {
                        error("base struct `" + base + "' not found");
                    }
                    if (s.getBaseStruct() != b) {
                        if (s.getBaseStruct() == null) {
                            error("native struct `" + s.getName() + "' has no father");
                        } else {
                            error("native struct `" + s.getName() + "' has a different father (`" + s.getBaseStruct() + "') with declaration here(`" + b + "')");
                        }
                    }
                }

                if (check('{')) {
                    /*
                     * Declaration of the the struct
                     * It does't check now by ignoring all character util '}'
                     */
                    checkExtensionDefinintion(s);
                }
            } else {
                pl = new ArrayList<>();

                Type t = type();
                Token name = look;
                match(Tag.ID);
                if (clazzName == null) {
                    clazzName = name.toString();
                }
                match('(');
                if (!check(')')) {
                    do {
                        Type vt = type();
                        Token n = look;
                        match(Tag.ID);
                        pl.add(new Param(vt, n));
                    } while (check(','));
                    match(')');
                }

                FunctionBasic f = null;
                try {
                    f = LoadFunc.loadFunc(t, sb.toString(), clazzName, name, pl, this.lex, this);
                } catch (Exception e) {
                    error("failed to load extension function `" + sb.toString() + "." + clazzName + "'", e);
                }
                if (!table.addFunc(name, f)) {
                    error("function name has conflict:" + name);
                }
            }
            match(';');
        }
    }

    @Override
    public Type getType(Token tok) {
        if (tok == Word.Auto)
            return null;
        return typeTable.get(tok);
    }

    private Type getAutoType(Token tok) {
        return typeTable.get(tok);
    }

    private void defStruct() throws IOException {
        match(Tag.STRUCT);

        /*
         * Struct definition:
         *      struct name {
         *          type name;[...]
         *      }
         */

        Token name = look;
        match(Tag.ID);
        Struct s = (Struct) getType(name);
        final boolean hasDeclared = s != null;
        final boolean isPreDecl = hasDeclared && !s.isClosed();//it is predeclared but not defined before,we just define it or just declare it again
        final boolean isDeclaration = hasDeclared && s.isClosed();// Declaration was defined before,we can only just declared it again
        if (check(':')) {
            Token base = look;
            match(Tag.ID);
            Type b = getType(base);
            if (b == null) {
                error("base struct `" + base + "' not found");
            }
            if (s != null) {//it is declared before we just check if it matched the signature
                if (s.getBaseStruct() == null) {
                    error("declared struct `" + s + "' has no base struct,but found `" + base + "'");
                } else if (!s.getBaseStruct().isCongruentWith(b)) {
                    error("declared struct `" + s + "' has a base struct `" + s.getBaseStruct() + "',but found `" + base + "'");
                }
            } else {
                s = new Struct(name, (Struct) b);
            }
        } else {
            if (s == null) {//it is not declared before
                s = new Struct(name);
            } else if (s.getBaseStruct() != null) {
                error("declared struct `" + s + "' has a base struct `" + s.getBaseStruct() + "',but not found in this definition");
            }
        }
        //now s is a new strut ready to be filled
        if (!hasDeclared) {
            defType(name, s);
        }

        dStruct = s;
        if (check(';')) {
            return;
        }
        if (isDeclaration) {
            error("struct `" + name + "' redefined");
        }
        parseStructBody(s);
        s.iteratorDerivedStruct().forEachRemaining(Struct::copyBase);
        dStruct = null;
    }

    private void parseStructBody(Struct struct) throws IOException {
        match('{');
        if (!(struct.getBaseStruct() == null || struct.getBaseStruct().isClosed())) {
            error("predeclared base struct `" + struct.getBaseStruct() + "' of `" + struct + "' is not closed yet");
        }
        struct.close();
        while (!check('}')) {
            Token op = null;
            if (check('@')) {
                if (look.tag == Tag.ID) {
                    op = getType(copymove());
                    if (op == null) {
                        error("type `" + look + "' not found");
                        return;
                    }
                } else {
                    op = copymove();
                }

                switch (op.tag) {
                    case '[':
                    case ']'://array binding
                        op = Word.array;
                        break;
                    case '(':
                    case ')'://functor
                        op = Word.brackets;
                        break;
                }

                if (op.tag == Tag.BASIC && look.tag == '[') {
                    Type t = arrtype((Type) op);
                    error("can't overloading array type `" + t + "' casting operand");
                }
            }
            /*Function definition*/
            if (check(Tag.DEF)) {
                defInnerStructFunction(struct, op);
            } else {
                if (op != null) {
                    error("overloading for `" + op + "' found but no function definition found");
                }
                Type t = type();
                Token m;
                do {
                    m = look;
                    match(Tag.ID);
                    if (struct.addVariable(m, t) != null) {
                        error("member `" + m.toString() + "' defined previously ");
                    }
                } while (check(','));
                match(';');
            }
        }
    }

    private void printFunctionDefinition(FunctionBasic f, Stmt s) {
        if (PRINT_FUNC_TRANSLATE) {
            System.out.println(f.toString() + "{");
            System.out.print(s.toString());
            System.out.println("}");
        }
    }

    /*
     * def a function in struct
     * s is the struct,op is the operand to overload(null for non-overloading)
     */
    private void defInnerStructFunction(Struct s, Token op) throws IOException {
        Env savedEnv = top;
        top = new Env(top);
        boolean savedHasDecl = hasDecl;
        hasDecl = true;
        Type savedType = returnType;
        isInStructInitialFunctionDefinition = (look == Word.This);

        Function f = (isInStructInitialFunctionDefinition) ? initialFunctionDeclaration(s, op) : innerFunctionDeclaration(s, op);

        if (!check(';')) {
            match('{');
            Stmt stmt = body();
            match('}');
            f.setBody(stmt);
            printFunctionDefinition(f, stmt);
        }
        isInStructInitialFunctionDefinition = false;
        top = savedEnv;
        returnType = savedType;
        hasDecl = savedHasDecl;
    }

    /*
     * def this(){
     *
     * }
     */
    private Function initialFunctionDeclaration(Struct s, Token op) throws IOException {
        if (op != null) {
            error("initial function can't be overloaded");
        }
        top.put(Word.This, s);
        match(Tag.ID);
        List<Param> l = parameters();
        l.add(0, new Param(s, Word.This));
        Function f = new InitialFunction(Word.This, l, s);
        s.defineInitialFunction(f);
        return f;
    }

    private Function innerFunctionDeclaration(Struct s, Token op) throws IOException {
        int flag = 0;
        boolean isDefaultSet = false;
        if(check(Tag.DEFAULT)){
            isDefaultSet = true;
        }

        if (check(Tag.VIRTUAL)) {
            flag = Tag.VIRTUAL;
        } else if (check(Tag.OVERRIDE)) {
            flag = Tag.OVERRIDE;
        }

        returnType = type();
        Token fname = look;
        match(Tag.ID);
        if (isForbiddenFunctionName(fname)) {
            error("Function name can't be `" + fname + "'");
        }

        /*pass `this' reference as the first argument*/
        top.put(Word.This, s);
        List<Param> l = parameters();
        l.add(0, new Param(s, Word.This));
        Function f = new MemberFunction(fname, returnType, l, s);
        if (flag == Tag.VIRTUAL) {
            s.defineVirtualFunction(fname, f);
        } else if (flag == Tag.OVERRIDE) {
            s.overrideVirtualFunction(fname, f);
        } else {
            if(isDefaultSet) {
                error("function `" + f + "' is default but not virtual");
            }
            s.addNaiveFunction(fname, f);
        }

        if(isDefaultSet){
            if (s.getDefaultFunctionName() != null && s.getDefaultFunctionName() != fname){
                error("function `" + s.getVirtualFunction(s.getDefaultFunctionName()) + "' is already default");
            }
            s.setDefaultFunctionName(fname);
        }
        if (op != null) {
            if (!s.addOverloading(op, f)) {
                error("operand `" + op + "' overloading is rebound");
            }
        }

        return f;
    }

    private void defFunction() throws IOException {
        match(Tag.DEF);
        Env savedEnv = top;
        top = new Env(top);
        boolean savedHasDecl = hasDecl;
        hasDecl = true;
        Type savedType = returnType;
        returnType = type();
        if (check('.')) {//may be the init function for struct definition
            if (!(returnType instanceof Struct)) {
                error("Struct name needed here,but found `" + returnType + "'");
            }
            Struct s = (Struct) returnType;
            Token name = look;
            match(Tag.ID);
            isInStructInitialFunctionDefinition = true;
            defOuterStructInitialFunction(s, name);
            isInStructInitialFunctionDefinition = false;
            top = savedEnv;
            returnType = savedType;
            hasDecl = savedHasDecl;
            return;
        }

        Token name = look;
        match(Tag.ID);

        if (check('.')) {
            Type t = getType(name);
            if (t == null) {
                error("struct `" + name + "' not found");
            }

            if (!(t instanceof Struct)) {
                error("struct type needed here,but `" + t + "' found");
                return;
            }
            name = look;
            match(Tag.ID);
            defOuterStructFunction((Struct) t, name, returnType);
            top = savedEnv;
            returnType = savedType;
            hasDecl = savedHasDecl;
            return;
        }

        List<Param> l = parameters();
        Function f;
        if (check(';')) {
            f = new Function(name, returnType, l);
            /*check if its name has been used*/
            if (!table.addFunc(name, f)) {
                error("function name has conflict:" + f);
            }
        } else {
            FunctionBasic fb = table.getFuncType(name);
            if (fb != null) {
                if (fb.isCompleted()) {
                    error("function redefined:" + fb);
                }
                f = (Function) fb;
                if (!f.getType().isCongruentWith(returnType)) {
                    error("function return type doesn't match with its former declaration:" + f + "\nnote: " + returnType);
                }
                if (f.getParamList().size() != l.size()) {
                    error("function arguments number doesn't match with its former declaration :" + new Function(name, returnType, l).toString() + " has " + l.size() + ",but " + f + " has " + f.getParamList().size());
                }
                int i = 0;
                for (Param t : f.getParamList()) {
                    if (!t.type.isCongruentWith(l.get(i).type)) {
                        error("function " + (new Function(name, returnType, l)).toString() + " has different parameters[" + i + "] types with its former declaration " + f);
                    }
                    i++;
                }
            } else {
                f = new Function(name, returnType, l);
                /*check if its name has been used*/
                if (!table.addFunc(name, f)) {
                    error("Function name has conflict:" + f);
                }
            }
            match('{');
            Stmt s = body();
            match('}');
            f.setBody(s);
            printFunctionDefinition(f, s);
        }

        top = savedEnv;
        returnType = savedType;
        hasDecl = savedHasDecl;
    }

    private void defOuterStructInitialFunction(Struct t, Token name) throws IOException {
        dStruct = t;
        if (name != Word.This) {
            error("unknown function name found here:`" + t.getName() + "." + name + "'");
        }
        InitialFunction f = (InitialFunction) t.getInitialFunction();
        if (f == null) {
            error("initial function declaration `" + t.getName() + ".[init]' not found");
            return;
        }
        if (f.isCompleted()) {
            error("initial function `" + t.getName() + ".[init]' redefined");
        }

        top.put(Word.This, t);
        List<Param> l = parameters();
        l.add(0, new Param(t, Word.This));
        if (l.size() != f.getParamList().size()) {
            error("arguments number of function `" + t.getName() + ".[init]' doesn't match with its former declaration:expect " + (f.getParamList().size() - 1) + " but found " + (l.size() - 1));
        }

        for (int i = 1; i < l.size(); i++) {
            if (!l.get(i).type.isCongruentWith(f.getParamList().get(i).type)) {
                error("function `" + t.getName() + ".[init]' has different parameters[" + (i - 1) + "] type `" + l.get(i).type + "' with its former declaration `" + f.getParamList().get(i).type + "'");
            }
        }
        match('{');
        Stmt stmt = body();
        match('}');
        f.setBody(stmt);

        printFunctionDefinition(f, stmt);
        dStruct = null;
    }

    private void defOuterStructFunction(Struct t, Token name, Type returnType) throws IOException {
        dStruct = t;

        //     if it is definition of the constructor
        Function f = (Function) t.getDeclaredFunction(name);
        if (f == null) {
            error("member function declaration `" + t.lexeme + "." + name + "' not found ");
            return;
        }
        /*
         * NOTE:type.isCongruentWith doesn't conclude
         * that father struct and child struct is the same
         */
        if (!f.getType().isCongruentWith(returnType)) {
            error("member function `" + t.lexeme + "." + name + "' return type doesn't match with the former declaration");
        }
        if (f.isCompleted()) {
            error("member function `" + t.lexeme + "." + name + "' redefined");
        }

        top.put(Word.This, t);
        List<Param> l = parameters();
        l.add(0, new Param(t, Word.This));
        if (l.size() != f.getParamList().size()) {
            error("arguments number of function `" + t.lexeme + "." + name + "' doesn't match with its former declaration");
        }
        for (int i = 1; i < l.size(); i++) {
            if (!l.get(i).type.isCongruentWith(f.getParamList().get(i).type)) {
                error("function `" + t.lexeme + "." + name + "' has different parameters[" + (i - 1) + "] type `" + l.get(i).type + "' with its former declaration `" + f.getParamList().get(i).type + "'");
            }
        }
        match('{');
        Stmt stmt = body();
        match('}');
        f.setBody(stmt);
        printFunctionDefinition(f, stmt);
        dStruct = null;
    }

    private List<Param> parameters() throws IOException {
        List<Param> pl = new ArrayList<>();
        match('(');
        if (!check(')')) {
            do {
                Type t = type();
                Token name = look;
                match(Tag.ID);
                if (getAutoType(name) != null) {
                    error("parameter `" + name + "' shadows a struct type");
                }
                pl.add(new Param(t, name));
                if (top.put(name, t) != null) {
                    error("function parameter names have conflict:`" + name.toString() + "'");
                }
            } while (check(','));
            match(')');
        }
        return pl;
    }

    private Stmt block() throws IOException {
        match('{');
        Env savedEnv = top;
        boolean savedHasDecl = hasDecl;
        hasDecl = false;
        //top = new Env(top);/*should not*/

        Stmt s = stmts();
        match('}');
        if (hasDecl) {
            s = new Seq(s, Stmt.RecoverStack);
            nowLevel--;
            top = savedEnv;
        }
        hasDecl = savedHasDecl;
        return s;
    }

    private Stmt body() throws IOException {
        Stmt stmt = stmts();
        return ENABLE_STMT_OPT ? stmt.optimize() : stmt;

    }

    private Stmt stmts() throws IOException {
        if (look.tag == '}') return Stmt.Null;
        LinkedSeq seq = new LinkedSeq();
        do {
            seq.append(stmt());
        } while (look.tag != '}');
        return seq;
    }

    public Stmt stmt() throws IOException {
        Expr x;
        Stmt s, s1, s2;
        Stmt savedStmt = Stmt.Enclosing, savedBreak = Stmt.BreakEnclosing;
        int savedLastIterationLevel = lastIterationLevel,
                savedLastBreakFatherLevel = lastBreakFatherLevel;
        switch (look.tag) {
            case ';':
                move();
                s = Stmt.Null;
                break;
            case Tag.IF:
                match(Tag.IF);
                match('(');
                x = expr();
                match(')');
                s1 = closure();
                if (look.tag == Tag.ELSE) {
                    match(Tag.ELSE);
                    s2 = stmt();
                    s = new Else(x, s1, s2);
                } else {
                    s = new If(x, s1);
                }
                break;
            case Tag.WHILE:
                lastIterationLevel = nowLevel;
                lastBreakFatherLevel = nowLevel;
                While whileNode = new While();
                Stmt.Enclosing = whileNode;
                Stmt.BreakEnclosing = whileNode;
                match(Tag.WHILE);
                match('(');
                x = expr();
                match(')');
                s1 = closure();
                whileNode.init(x, s1);
                s = whileNode;
                break;
            case Tag.DO:
                lastIterationLevel = nowLevel;
                lastBreakFatherLevel = nowLevel;
                Do doNode = new Do();
                Stmt.Enclosing = doNode;
                Stmt.BreakEnclosing = doNode;
                match(Tag.DO);
                s1 = closure();
                match(Tag.WHILE);
                match('(');
                x = expr();
                match(')');
                match(';');
                doNode.init(s1, x);
                s = doNode;
                break;
            case Tag.FOR:
                s = forloop();
                break;
            case Tag.BREAK:
                match(Tag.BREAK);
                match(';');
                s = new Break(nowLevel - lastBreakFatherLevel);
                break;
            case Tag.CONTINUE:
                match(Tag.CONTINUE);
                match(';');
                s = new Continue(nowLevel - lastIterationLevel);
                break;
            case Tag.RETURN:
                match(Tag.RETURN);
                Expr e = Expr.VoidExpr;
                if (returnType != Type.Void)
                    e = expr();
                match(';');
                s = new Return(e, returnType, nowLevel - lastFunctionLevel);
                break;
            case '{':
                s = block();
                break;
            case Tag.ID:
                Type t = getAutoType(look);
            /*check if it is just a variable*/
                if (t == null) {
                    s = new ExprStmt(expr());
                    match(';');
                    break;
                }
            /*or it should be variable definition*/
            case Tag.BASIC:
                if (hasDecl) {
                    s = decls();
                } else {
                    top = new Env(top);
                    nowLevel++;
                    hasDecl = true;
                    s = new Seq(Stmt.PushStack, decls());
                }
                break;
            case Tag.SWITCH:
                s = switchStmt();
                break;
            case Tag.DEFAULT:
            case Tag.CASE:/*CASE*/
                error("`" + look + "' should be used in switch statement");
                s = Stmt.Null;
                break;
            default:
            /*single expression statement*/
                s = new ExprStmt(expr());
                match(';');
        }
        Stmt.BreakEnclosing = savedBreak;
        Stmt.Enclosing = savedStmt;
        lastIterationLevel = savedLastIterationLevel;
        lastBreakFatherLevel = savedLastBreakFatherLevel;
        return s;
    }

    private Stmt casestmts() throws IOException {
        LinkedSeq s = new LinkedSeq();

        while (look.tag != Tag.CASE && look.tag != '}' && look.tag != Tag.DEFAULT) {
            s.append(stmt());
        }
        return s;
    }

    private Stmt casestmt() throws IOException {
        Env savedEnv = top;
        boolean savedHasDecl = hasDecl;
        hasDecl = false;
        Stmt s = casestmts();
        if (hasDecl) {
            s = new Seq(s, Stmt.RecoverStack);
            nowLevel--;
            top = savedEnv;
        }
        hasDecl = savedHasDecl;
        return ENABLE_STMT_OPT ? s.optimize() : s;
    }

    private Stmt switchStmt() throws IOException {
        match(Tag.SWITCH);
        match('(');
        //Available types for switch
        //string,int,char
        Expr condition = expr();
        Switch sw = Switch.getSwitch(condition);
        lastBreakFatherLevel = nowLevel;
        Stmt.BreakEnclosing = sw;
        match(')');
        match('{');
        while (look.tag != '}') {
            if (check(Tag.CASE)) {
                Expr c = expr().optimize();
                if (!(c instanceof Value)) {
                    error("case expression `" + c + "' is not constant");
                    return null;
                }

                Value val = (Value) c;

                if (sw.isCaseSet(val)) {
                    error("case `" + c + "' has been handled before");
                }

                match(':');

                sw.appendCase(val, casestmt());
            } else if (check(Tag.DEFAULT)) {
                match(':');
                if (sw.isDefaultSet()) {
                    error("default case reoccurs");
                }
                /*stmts should be different from the normal stmts*/
                sw.setDefault(casestmts());
            } else {
                error("wrong symbol found in switch:`" + look + "'");
            }
        }
        match('}');
        return sw;
    }

    private Stmt closure() throws IOException {
        if (look.tag == '{') {
            return block();
        } else {
            Env savedEnv = top;
            boolean savedHasDecl = hasDecl;
            hasDecl = false;
            Stmt s = stmt();
            if (hasDecl) {
                s = new Seq(s, Stmt.RecoverStack);
                nowLevel--;
                top = savedEnv;
            }
            hasDecl = savedHasDecl;
            return s;
        }
    }

    /**
     * We put for-loop is like:<br/>
     * 1.PushStack ;<br/>
     * 2.For-decl;<br/>
     * 3.condition;<br/>
     * 4.for-body<br/>
     * 5.end;<br/>
     * 6.RecoverStack;<br/>
     * if it breaks,it will go to 6<br/>
     * if it continues,it will goto 5
     *
     * @return the loop statement
     */
    private Stmt forloop() throws IOException {
        For fornode = new For();
        Stmt.Enclosing = fornode;
        Stmt.BreakEnclosing = fornode;
        move();
        match('(');
        Stmt s1;
        Env savedTop = top;
        boolean hasdecl = false;
        if (look.tag == ';') {
            s1 = Stmt.Null;
        } else if (look.tag == Tag.BASIC || getAutoType(look) != null) {
            hasdecl = true;
            top = new Env(top);
            s1 = fordecl();
            nowLevel++;
        } else {
            s1 = new ExprStmt(expr());
        }
        lastIterationLevel = nowLevel;
        lastBreakFatherLevel = nowLevel;
        match(';');
        Expr e2 = (look.tag == ';') ? Value.True : expr();
        match(';');

        LinkedSeq s3 = new LinkedSeq();

        if (look.tag != ')') {
            do {
                s3.append(new ExprStmt(expr()));
            } while (check(','));
        }
        match(')');
        Stmt s = closure();
        top = savedTop;
        fornode.init(s1, e2, s3, s);
        if (hasdecl)
            nowLevel--;
        s = hasdecl ? new Seq(Stmt.PushStack, new Seq(fornode, Stmt.RecoverStack)) : fornode;
        return s;
    }

    /**
     * Infer the type of variable {@param id}<br/>
     * If it is impossible to infer,throw an exception
     *
     * @param id the variable name
     * @param p  the expression to initialize the {@param id}
     * @return the type inferred bt the expression
     */
    private Type inferType(Token id, Expr p) {
        if (p == null || p == Value.Null) {
            error("Can't infer the type of variable `" + id + "'");
            return null;
        }
        return p.type;
    }

    /**
     * For loop declaration parsing,supporting following syntax:([]for option,supposing that it should be declaration here)<br/>
     * type id1[=?][,id2...]
     *
     * @return the declarations statement
     * @throws IOException when io fails,{@linkplain #move()}
     */
    private Stmt fordecl() throws IOException {
        /*
         * for(int i = 0,j = 0;;)
         */
        Decls s = new Decls();
        Type p = autoType();
        Token tok;
        if (p == Type.Void) {
            error("can't declare " + p.toString() + " type variable");
        }
        do {
            Expr e = null;
            tok = look;
            match(Tag.ID);
            if (top.containsVar(tok)) {
                error("variable `" + tok.toString() + "' redefined here");
            }
            if (getAutoType(tok) != null) {
                error("variable `" + tok + "' shadows a struct type");
            }
            if (check('=')) {
                e = expr();
            }
            if (p == Type.Auto) {//just infer once,every variable's type depends on 1st expr;
                p = inferType(tok, e);
            }
            top.put(tok, p);

            s.addDecl(Decl.getDecl(tok, p, e));
        } while (check(','));

        return s;
    }

    private Decls decls() throws IOException {
        Decls s = new Decls();
        Expr e;
        //while( look.tag == Tag.BASIC){
        Type p = autoType();
        if (p == Type.Void) {
            error("can't declare " + p.toString() + " type variable");
        }
        Token tok;
        do {
            e = null;
            tok = look;
            match(Tag.ID);
            if (top.containsVar(tok)) {
                error("variable `" + tok.toString() + "' redefined here");
            }

            if (getAutoType(tok) != null) {
                error("variable `" + tok + "' shadows a struct type");
            }
            if (check('=')) {
                //deal with the initialization of array
                if (p instanceof Array && look.tag == '{') {
                    e = initiallist((Array) p);
                } else {
                    e = expr();
                }
            }

            if (p == Type.Auto) {
                p = inferType(tok, e);
            }

            top.put(tok, p);

            s.addDecl(Decl.getDecl(tok, p, e));
        } while (check(','));
        match(';');
        //}
        return s;
    }

    private Expr initiallist(Array p) throws IOException {
        /*
         * initlist -> {list}
        * list -> element | list,element
        * element -> Expr | initlist
        * Constraint:in `Y -> Y,B',Type Y must match with B
        */
        match('{');
        Expr e = list(p.of);
        match('}');
        return e;
    }

    public Expr list(Type p) throws IOException {
        Expr initseq = Value.Null;

        List<Expr> init_list = new ArrayList<>();
        if (look.tag != '}') {
            do {
                init_list.add(element(p));
            } while (check(','));
        }

        Expr arrdefine = new NewArray(p, p, new Value(init_list.size()));
        InPipe in = new InPipe(arrdefine);
        OutPipe out = new OutPipe(in);
        if (!init_list.isEmpty()) {
            initseq = new inter.expr.Set(Word.ass, new ArrayVar(out, p, new Value(0)), init_list.get(0));
        }

        for (int i = 1; i < init_list.size(); i++) {
            initseq = new SeqExpr(Word.array, initseq, new inter.expr.Set(Word.ass, new ArrayVar(out, p, new Value(i)), init_list.get(i)));
        }
        //for each initseq
        return initseq == Value.Null ? arrdefine : new SeqExpr(Word.array, in, initseq);
    }

    private Expr element(Type p) throws IOException {
        Expr e;
        if (look.tag == '{') {//array of array
            //p must be array
            if (p instanceof Array) {
                e = initiallist((Array) p);
            } else {
                //TODO:details
                error("too many dimensions than the array declaration");
                return null;
            }
        } else {
            e = expr();
        }
        Expr tmp = e;
        if (!e.type.isCongruentWith(p)) {
            e = ConversionFactory.getConversion(e, p);
        }
        if (e == null) {//type error
            error("array init error:can't convert `" + tmp.type + "' to `" + p + "'");
        }
        return e;
    }

    /*
     * match single type (except for array)
     */
    private Type singleType() throws IOException {
        if (look == Word.Auto) {//check
            error("auto type infer is not permitted here ");
        }

        if (look.tag == Tag.ID) {
            Token t = getType(look);
            if (t == null) {
                error("type `" + look + "' not found");
                return null;
            }
            look = t;
        }
        Type p = null;
        //System.err.println("LOOK " + look);
        if (look.tag == Tag.BASIC) {
            p = (Type) copymove();
        } else {
            error("type name wanted here,but found `" + look + "' ");
        }
        return p;
    }

    private Type autoType() throws IOException {
        if (look == Word.Auto) {
            move();
            return Type.Auto;
        }
        return type();
    }

    private Type type() throws IOException {
        Type p = singleType();

        if (look.tag == '[') {
            if (p == Type.Void) {
                error("type `" + p + "' can't be element type of array");
            }
            return arrtype(p);
        }
        return p;
    }

    private Array arrtype(Type of) throws IOException {
        match('[');
        match(']');
        Array a = new Array(of);
        while (check('[')) {
            match(']');
            a = new Array(a);
        }
        return a;
    }

    public Expr expr() throws IOException {
        Expr e = rawExpr();
        return ENABLE_EXPR_OPT ? e.optimize() : e;
    }

    private Expr rawExpr() throws IOException {
        return assign();
    }

    private Expr assign() throws IOException {
        Expr l = condition();
        while (look.tag == '=' || look.tag == Tag.ADDASS || look.tag == Tag.MINASS
                || look.tag == Tag.MULTASS || look.tag == Tag.DIVASS || look.tag == Tag.MODASS) {
            l = SetFactory.getSet(copymove(), l, condition());
        }
        return l;
    }

    private Expr condition() throws IOException {
        Expr e = bool();
        Token t = look;
        if (check('?')) {
            Expr iftrue = rawExpr();
            match(':');
            Expr iffalse = condition();
            e = new Condition(t, e, iftrue, iffalse);
        }
        return e;
    }

    public Expr bool() throws IOException {
        Expr l = join();
        while (look.tag == Tag.OR) {
            l = new Or(copymove(), l, join());
        }
        return l;
    }

    private Expr join() throws IOException {
        Expr l = equality();
        while (look.tag == Tag.AND) {
            l = new And(copymove(), l, equality());
        }
        return l;
    }

    private Expr equality() throws IOException {
        Expr l = rel();
        if (look.tag == Tag.EQ || look.tag == Tag.NE) {
            l = RelFactory.getRel(copymove(), l, rel());
        }
        return l;
    }

    private Expr rel() throws IOException {
        Expr l = typeCheck();
        if (look.tag == Tag.GE || look.tag == Tag.LE
                || look.tag == '<' || look.tag == '>') {
            l = RelFactory.getRel(copymove(), l, typeCheck());
        }
        return l;
    }

    private Expr typeCheck() throws IOException {
        Expr e = add();
        while (look.tag == Tag.INSTOF) {
            Token tok = copymove();
            Type t = type();
            e = new IsInstanceOf(tok, e, t);
        }
        return e;
    }

    public Expr add() throws IOException {
        Expr l = mult();
        while (look.tag == '+' || look.tag == '-') {
            l = ArithFactory.getArith(copymove(), l, mult());
        }
        return l;
    }

    private Expr mult() throws IOException {
        Expr l = unary();
        while (look.tag == '*' || look.tag == '/' || look.tag == '%') {
            l = ArithFactory.getArith(copymove(), l, unary());
        }
        return l;
    }

    private Expr unary() throws IOException {
        switch (look.tag) {
            case '!':
                return new Not(copymove(), unary());
            case Tag.SIZEOF:
                return new SizeOf(copymove(), unary());
            case '-':
            case Tag.INC:
            case Tag.DEC:
                return UnaryFactory.getUnary(copymove(), unary());
            default:
                return postInc();
        }
    }

    private Expr postInc() throws IOException {
        Expr e = postfix();
        switch (look.tag) {
            case Tag.INC:
            case Tag.DEC:
                return PostUnaryFactory.getUnary(copymove(), e);
            default:
                return e;
        }
    }

    private Expr postfix() throws IOException {
        boolean saved = isSuperCalled;
        isSuperCalled = false;
        Expr e = factor();//factor() will change is supper called
        switch (look.tag) {
            case '[':
            case '.':
                e = access(e);
        }
        isSuperCalled = saved;
        return e;
    }

    private Expr access(Expr e) throws IOException {
        do {
            if (look.tag == '.') {
                match('.');
                e = member(e);
            } else {
                isSuperCalled = false;
                e = offset(e);
            }
            isSuperCalled = false;
        } while (look.tag == '[' || look.tag == '.');
        return e;
    }

    private Expr member(Expr e) throws IOException {
        Token mname = look;
        match(Tag.ID);
        if (look.tag == '(') {
            List<Expr> args = arguments();
            if (!(e.type instanceof Struct))
                error("member function is for struct,not for `" + e.type + "'");
            Struct s = (Struct) (e.type);
            FunctionBasic f = s.getNaiveFunction(mname);
            if (f == null) {
                f = s.getVirtualFunction(mname);
                if (f == null)
                    error("member function `" + s + "." + mname + "' not found");
                fUsed.add(f);
                /*Pass `this' reference as the first argument*/
                if (isSuperCalled) {
                    args.add(0, e);
                    return new FunctionInvoke(f, args);
                }
                return new VirtualFunctionInvoke(e, f, args);
            } else {
                fUsed.add(f);
                /*Pass `this' reference as the first argument*/
                args.add(0, e);
                return new FunctionInvoke(f, args);
            }
        }
        return new StructMemberAccess(e, mname);
    }

    private Expr offset(Expr e) throws IOException {
        match('[');
        /*check type*/
        Expr loc = expr();
        match(']');
        /*if it is string Item access*/
        if (e.type == Type.Str) {
            if (e instanceof Var) {
                e = new StringVarAccess((Var) e, loc);
            } else {
                e = new StringAccess(e, loc);
            }
            return e;
        } else if (e.type instanceof Struct) {
            Struct s = (Struct) e.type;
            Token overloading = s.getOverloading(Word.array);
            if (overloading == null) {
                error("struct `" + s.getName() + "' overloading function for operand `" + Word.array + "' not found!");
            }
            FunctionBasic f = s.getVirtualFunction(overloading);
            if (f != null) {
                return new VirtualFunctionInvoke(e, f, new ArrayList<>());
            }
            f = s.getNaiveFunction(overloading);
            assert f != null;
            return new FunctionInvoke(f, Arrays.asList(e, loc));
        } else if (!(e.type instanceof Array)) {//update:remove the judge that e must be a variable
            error("operand `[]` should be used for array type or string,not for " + e.type);
        }

        Type t = ((Array) (e.type)).of;/*element type*/

        e = new ArrayVar(e, t, loc);
        /*for string index access*/
        return e;
    }

    private Expr factor() throws IOException {

        switch (look.tag) {
            case Tag.ID:
                Token tmp = copymove();

                if (look.tag == '(') {
                    return invocation(tmp);
                }
                Token name = tmp;
                if (tmp == Word.Super) { //This IS a kind of super!
                    isSuperCalled = true;
                    name = Word.This;
                }
                EnvEntry ee = top.get(name);
                if (ee == null) {
                /*
                 * if it is in struct function definition
                 */
                    //if((ee = top.get(Word.This)) != null ){
                    //    Expr pthis = ee.stacklevel == 0? new AbsoluteVar(tmp,ee.type,0,ee.offset) : new Var(tmp,ee.type,top.level - ee.stacklevel,ee.offset);
                    //    return member(pthis);
                    //}
                    error("variable `" + tmp + "' not declared");
                    return null;
                }
            /*
             * Level 0 is for the global variables
             * By default,use offset to present stack level to get var
             * address in runtime stack<stackoffset,varoffset>
             * But for the global variable,we can't know what it is
             * exactly in functions.So we use the AbsoluteVar<stackbackoffset,varoffset>.
             */
                Type t = ee.type;
                if (isSuperCalled) {// it must be a struct
                    t = ((Struct) (t)).getBaseStruct();
                    if (t == null) {
                        error("`" + ee.type + "' has no super struct defined");
                    }
                }
                if (ee.stacklevel <= lastFunctionLevel && ee.stacklevel != 0) {
                    Token identifier = lex.getOrReserve("@" + name);
                    StructVariable var = dStruct.getVariable(identifier);
                    if (var == null) {
                        assert dStruct.addVariable(identifier, ee.type) == null;
                        var = dStruct.getVariable(identifier);
                        assert capturedVars.add(name);
                    }

                    assert var.type.isCongruentWith(ee.type);
                    //error("anonymous variable capture is not supported now:found variable `" + tmp + "'");
                    EnvEntry this_ = top.get(Word.This);
                    return new StructMemberAccess(new StackVar(tmp, dStruct, top.level - this_.stacklevel, this_.offset), identifier).readOnly();
                }
                return StackVar.stackVar(tmp, t, ee.stacklevel, ee.offset, top.level);
            case Tag.NULL:
                move();
                return Value.Null;
            case Tag.TRUE:
                move();
                return Value.True;
            case Tag.FALSE:
                move();
                return Value.False;
            case Tag.NUM:
                return new Value(copymove(), Type.Int);
            case Tag.STR:
                return new Value(copymove(), Type.Str);
            case Tag.FLOAT:
                return new Value(copymove(), Type.Real);
            case Tag.CHAR:
                return new Value(copymove(), Type.Char);
            case Tag.BIGNUM:
                return new Value(copymove(), Type.BigInt);
            case Tag.BIGFLOAT:
                return new Value(copymove(), Type.BigReal);
            case Tag.NEW:
                Token l = copymove();
                //match('<');
                t = singleType();
                if (t == Type.Void) {
                    error("can't use type `" + t + "'");
                }
                //match('>');
                if (check('[')) {
                    Expr e = null;
                    do {
                        if (!check(']')) {
                            e = rawExpr();
                            match(']');
                            break;
                        }
                        t = new Array(t);
                    } while (check('['));

                    if (e == null) {
                        error("unknown array allocation size:" + t);
                    }

                    return new NewArray(l, t, e);
                } else {// it is `new' struct
                    if (t instanceof Struct) {
                        Node line = new Node();
                        List<Expr> args = null;
                        boolean isLambda = false;
                        if (look.tag == '(') {//has initial function
                            args = arguments();
                        } else if (look.tag == '^' || look.tag == '-') {
                            //new T^(params)->{stmt}:ambiguous: ternary operator ?:
                            //new T^(params)->expr
                            //new T->{stmt};
                            //new T->expression;
                            t = lambdaExpression((Struct) t);
                            isLambda = true;
                        }
                        //new T{}
                        if (look.tag == '{' && !isLambda) {
                            t = anonymousInnerStruct((Struct) t);
                        }
                        if (args == null && null != ((Struct) t).getInitialFunction()) {
                            if (((Struct) t).getInitialFunction().getParamSize() != 1) {//
                                assert ((Struct) t).getInitialFunction().getParamSize() > 1;
                                line.error("`" + t + "' has a defined initial function");
                            }
                            args = new ArrayList<>();
                        }

                        ((Struct) t).setInstantiated(Lexer.line, Lexer.offset, Lexer.filename);
                        if (!((Struct) t).isClosed()) {
                            line.error("try to instantiate a struct `" + t + "' which is not closed yet");
                        }
                        Expr eNew = new New(l, (Struct) t);
                        if (args != null) {
                            InPipe ipNew = new InPipe(eNew);
                            eNew = ipNew;
                            Expr init = new OutPipe(ipNew);
                            args.add(0, init);
                            FunctionBasic f = ((Struct) t).getInitialFunction();
                            if (f == null) {
                                line.error("`" + t + "' doesn't have an initial function");
                            }
                            eNew = new SeqExpr(Word.This, eNew, new FunctionInvoke(f, args));
                        }
                        return eNew;

                    } else {
                        error("new " + t + " is not permitted:`" + t + "' is not a struct type");
                        return null;
                    }
                }
            case '(':
                return cast();
            default:
                error("unexpected token found:" + look);
                return null;
        }
    }

    private Expr cast() throws IOException {
        Expr e;
        match('(');
        switch (look.tag) {
            case Tag.ID:
                Type t1 = getType(look);
            /*check if it is just a variable*/
                if (t1 == null) {
                    e = rawExpr();
                    match(')');
                    break;
                }
                look = t1;
            case Tag.BASIC:
                Type t = type();
                match(')');
                Expr f = unary();
                assert (f != null);
                e = f;
                if (!f.type.isCongruentWith(t))
                    e = ConversionFactory.getAutoDownCastConversion(f, t);
                if (e == null)
                    error("can't convert " + f.type + " to " + t);
                break;
            default:
                e = rawExpr();
                match(')');
                break;
        }
        return e;
    }

    private Expr invocation(Token id) throws IOException {
        FunctionBasic f = null;
        List<Expr> args = arguments();
        if (id == Word.Super) {
            Struct fs;
            if (dStruct == null || !isInStructInitialFunctionDefinition) {
                error("Keyword `" + id + "' can be only used in the struct initial function");
            }
            fs = dStruct.getBaseStruct();
            if (fs == null) {
                error("`" + dStruct + "' has no father");
                return null;
            }
            f = fs.getInitialFunction();
            if (f == null) {
                error("`" + fs + "'(father of `" + dStruct + "') has no declared initial function");
            }
            Expr pthis;
            EnvEntry ee;
            ee = top.get(Word.This);
            assert (ee != null);
            pthis = new StackVar(dStruct, ee.type, top.level - ee.stacklevel, ee.offset);
            args.add(0, pthis);
        } else {
            f = table.getFuncType(id);
            if (f == null) {
                final EnvEntry ee = top.get(id);
                if (ee != null && ee.type instanceof Struct) {
                    Token overloading = ((Struct) ee.type).getOverloading(Word.brackets);
                    if (overloading != null) {
                        f = ((Struct) ee.type).getVirtualFunction(overloading);
                        if (f != null) {
                            return new VirtualFunctionInvoke(StackVar.stackVar(id, ee, top.level), f, args);
                        }
                        f = ((Struct) ee.type).getNaiveFunction(overloading);
                        args.add(0, StackVar.stackVar(id, ee, top.level));
                    }
                }
            }
        }
        if (f == null) {
            error("function `" + id + "' not found.");
        }
        return new FunctionInvoke(f, args);
    }

    private List<Expr> arguments() throws IOException {
        match('(');
        List<Expr> p = new ArrayList<>();
        if (!check(')')) {
            do {
                p.add(expr());
            } while (check(','));
            match(')');
        }
        return p;
    }

    private void generateCaptures(Struct savedDStruct, Set<Token> savedCaptures, int savedLastFunctionLevel) {
        symbols.Struct base = dStruct.getBaseStruct();
        if (dStruct.getInitialFunction() == null) {
            dStruct.defineInitialFunction(base.getInitialFunction());
        }

        if (dStruct.getInitialFunction() == null && !capturedVars.isEmpty()) {
            dStruct.defineInitialFunction(new InitialFunction(Word.This, Collections.singletonList(new Param(dStruct, Word.This)), dStruct, new LinkedSeq()));
        }
        if (!capturedVars.isEmpty()) {
            InitialFunction init = (InitialFunction) dStruct.getInitialFunction();
            LinkedSeq body = new LinkedSeq();
            final Var arg0 = new StackVar(Word.This, dStruct, 0, 0);

            for (Token name : capturedVars) {
                EnvEntry ee = top.get(name);
                assert ee != null;
                if (ee.stacklevel != 0) {

                    Token identifier = lex.getOrReserve("@" + name);
                    if (ee.stacklevel <= savedLastFunctionLevel) {//outer nested inner struct
                        assert savedDStruct != null;
                        StructVariable var = savedDStruct.getVariable(identifier);
                        if (var == null) {
                            savedDStruct.addVariable(identifier, ee.type);
                            var = savedDStruct.getVariable(identifier);
                            assert savedCaptures.add(name);
                        }

                        assert var.type.isCongruentWith(ee.type);

                        EnvEntry outerThis_ = top.get(Word.This);

                        body.append(new ExprStmt(new inter.expr.Set(Word.ass,
                                new StructMemberAccess(arg0, identifier),
                                new StructMemberAccess(new StackVar(Word.This, savedDStruct, top.level - outerThis_.stacklevel + 1, outerThis_.offset), identifier).readOnly())));
                    } else {//the recursive exit gate
                        body.append(new ExprStmt(new inter.expr.Set(Word.ass,
                                new StructMemberAccess(arg0, identifier),
                                //NOTE : this code is running in an initial function so we need add an extra level
                                new StackVar(name, ee.type, top.level - ee.stacklevel + 1, ee.offset).readOnly())));
                    }
                }
            }

            body.append(init.getBody());
            init.setBody(body);
            printFunctionDefinition(init, body);
        }
    }

    private Struct anonymousInnerStruct(Struct base) throws IOException {
        Set<Token> savedCaptures = capturedVars;
        capturedVars = new HashSet<>();
        int savedLastFunctionLevel = lastFunctionLevel;
        lastFunctionLevel = top.level;
        Struct savedDStruct = dStruct;
        final Struct ais = new Struct(lex.getOrReserve("AISof" + base.getName() + "#" + (anonymousInnerStructId++) + Lexer.line + Lexer.offset), base);
        dStruct = ais;
        //starts with a '{' and ends with a matched '}'
        parseStructBody(dStruct);

        generateCaptures(savedDStruct, savedCaptures, savedLastFunctionLevel);

        FunctionBasic f;
        if ((f = dStruct.getFirstUncompletedFunction()) != null) {
            error("Anonymous inner struct of `" + base + "' is not completed:" + f);
        }
        //put the functions
        assert (null == defType(dStruct.getName(), dStruct));
        this.dStruct = savedDStruct;
        lastFunctionLevel = savedLastFunctionLevel;
        capturedVars = savedCaptures;
        return ais;
    }


    private Struct lambdaExpression(Struct base) throws IOException {
        if (base.getInitialFunction() != null && base.getInitialFunction().getParamSize() != 1) {
            error("lambda expression needs no initial function or without parameters");
        }

        FunctionBasic f = base.getVirtualFunction(base.getDefaultFunctionName());
        if (f == null) {
            error("default function not found for struct `" + base.getName() + "'");
            return base;//avoiding warning
        }
        Set<Token> savedCaptures = capturedVars;
        capturedVars = new HashSet<>();
        int savedLastFunctionLevel = lastFunctionLevel;
        lastFunctionLevel = top.level;
        Struct savedDStruct = dStruct;
        Struct lambda = new Struct(lex.getOrReserve("lambda$" + base.getName() + "#" + (anonymousInnerStructId++) + Lexer.line + Lexer.offset), base);
        dStruct = lambda;
        //^(params)->{stmt}
        //^(params)->expr
        //->{stmt};
        //->expression;
        List<Param> params = new ArrayList<>();
        params.add(new Param(dStruct, Word.This));
        Iterator<Param> paramIterator = f.getParamList().iterator();
        assert paramIterator.hasNext();
        paramIterator.next();//this reference
        switch (copymove().tag) {
            case '^':
                match('(');
                if (!check(')')) {
                    do {
                        Token id = look;
                        match(Tag.ID);
                        if (getAutoType(id) != null) {
                            error("parameter `" + id + "' shadows a struct type");
                        }
                        if (!paramIterator.hasNext()) {
                            error("unexpected parameter `" + id + "'[" + params.size() + "]");
                        }
                        Param p = paramIterator.next();
                        params.add(new Param(p.type, id));
                    } while (check(','));
                    match(')');
                    // NOTE:Ignoring useless parameter is permitted,
                    // but we still need to push and occupy the stack space
                    paramIterator.forEachRemaining(p -> params.add(new Param(p.type, lex.getOrReserve("#" + p.name))));
                }
                match('-');
            case '-':
                match('>');
                break;
        }
        Type savedReturnType = returnType;
        returnType = f.getType();
        Stmt body = parseLambdaExpressionBody(params);
        returnType = savedReturnType;
        dStruct.overrideVirtualFunction(f.getName(), new MemberFunction(f.getName(), f.getType(), params, dStruct, body));
        generateCaptures(savedDStruct, savedCaptures, savedLastFunctionLevel);
        dStruct.close();
        dStruct = savedDStruct;
        lastFunctionLevel = savedLastFunctionLevel;
        capturedVars = savedCaptures;
        return lambda;
    }

    public Stmt parseLambdaExpressionBody(List<Param> parameters) throws IOException {
        Env savedEnv = top;
        top = new Env(top);
        boolean savedHasDecl = hasDecl;
        hasDecl = true;
        parameters.forEach(p -> {
            if (top.put(p.name, p.type) != null) {
                error("lambda expression parameters redeclared:`" + p.name + "'");
            }
        });
        final Stmt s;
        if (check('{')) {
            s = body();
            match('}');
        } else {
            s = new Return(expr(), returnType, 0);
        }

        hasDecl = savedHasDecl;
        top = savedEnv;
        return s;
    }
}