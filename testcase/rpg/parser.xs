/*
* RPGScript -> E | Statement RPGScript
* Statement -> <VAL> Statement | Command 
* Command -> VAL : VAL-LIST \n
* VAL-LIST = E | VAL VAL-LIST
* SUPPORTED function:
"sleep":seconds
"jump":label
"condition": varname value jumplabel
"dialog": content color name
"choice": varname val,...
"set":varname val
"set_global":varname val
"add_num":varname val
"open":val

while( condition ) {
	do
}
      || ||
	  || ||
	  || ||
<"beg">"condition" varname value "a"
"jump":"end" 
<"a">
 
"jump":"beg"
<"end">
*/

import "../container/content.xs";
import "../container/hashmap.xs";
import "../container/darray.xs";
import "../lib/system.xs";

struct FunctionBasic:Content{}

struct RuntimeBasic {
	//HashMap globalVarMap;//<string,string>
	HashMap varMap;//<string,string>
	HashMap labelMap;//<string,integer>
	int index;
	
	def this(){
		this.varMap = new HashMap();
		this.labelMap = new HashMap();
		int index = 0;
	}
	
	def string getVar(string varname){
		return ((StringHashContent)(this.varMap).getVar( new StringHashContent(varname) ));
	}
	
	def void setVar(string varname,string val){
		this.varMap.setVar( new StringHashContent(varname), new StringContent(val) );
	}
    
	def void jump(string label){
		auto l = (IntContent) this.labelMap.get(new StringHashContent(label));
		this.index = (int)l - 1;
	}
	
	def void sleep(int second){
		sleep(second);
	}
	
	def virtual void open(string filename);
	
	def virtual void step();
}

struct Function:FunctionBasic{
    def virtual void run(RuntimeBasic r,string[] args);
	
	def override string toString(){
		return "";
	}
}


struct Sleep:Function{
	def override void run(RuntimeBasic r,string[] args){
		
	}
}

struct Set:Function{
	def override void run(RuntimeBasic r,string[] args){
		r.setVar(args[0],args[1]);
	}
}

struct Condition:Function{
	def override void run(RuntimeBasic r,string[] args){
		if(r.getVar(args[0]) == args[1]){
			r.jump(r.args[2]);
		}
	}
}


struct RuntimeBasic_ : RuntimeBasic{
	HashMap functionMap;//<string,Function>
	def this(){
        super();
		this.functionMap = new HashMap();
	}

	def Function getFunction(string funcname){
       return (Function)this.functionMap.get(new StringHashContent(funcname))
	}
	
    def void registerFunction(string funcname,Function function){
       this.functionMap.set(new StringHashContent(funcname),function);
	}

}



struct Instruction:Content{
	string[] args;
	string function;
	
	def this(string[] args,string function){
		this.args = args;
		this.function = function;
	}

    def void run(RuntimeBasic_ r){
        r.getFunction(function).run(r,args);
    }
    
	def override string toString(){
		return function;
	}
	
}

struct Runtime:RuntimeBasic_{
	DynamicArray instructions;

	
	def this(){
		super();
		this.instructions = new DynamicArray();
	}
	
    def void addLable(string label){
        this.labelMap.set(new StringHashContent(label),new IntContent( this.instructions.size()));
    }
    
	def void addInstructions2(string[] args,string function){
		this.instructions.add(new Instruction(args,function));
	}
	
	def void addInstructions(Instruction i){
		this.instructions.add(i);
	}
	
	def void clearInstructions(){
		this.instructions.clear();
	}
    
	def override void step(){
		((Instruction)this.instructions).get(this.index);
		this.index++;
	}
}

struct RPGToken:Content{
    def override string toString(){
        return "";
    }
}

struct ValueToken:RPGToken{
    string value;
    def this(string str){
        this.value = str;
    }

    def override string toString(){
        return this.value;
    }
}

struct SymbolToken:RPGToken{
    int value;
    def this(int c){
        this.value = c;
    }
    
    def override string toString(){
        return (string)this.value;
    }
}

struct RPGLexer{
    File f;
    int peak;
    
    def this(){
        this.peak = ' ';
    }
    
    def void open(string filename){}
    
    def bool check(int c){
        if(c == this.peak){
            readch();
            return true;
        }
        return false;
    }
    
    def void readch(){
        peak = this.f.readch();
    }
    
    def Token scan(){
        Token t;
        while(this.check('\t')||this.check(' '));
        
        switch(this.peak){
        case '\"':
            string str;
            this.readch();
            while(!this.check('\"')){
                str += this.peak;
                this.readch();
            }
            t = new ValueToken(str);
            break;
        default:
            t = new SymbolToken(this.peak);
            this.readch();
        }
        return t;
    }
}

/*
* RPGScript -> E | Statement RPGScript
* Statement -> <VAL> Statement | Command 
* Command -> VAL : VAL-LIST \n
* VAL-LIST = E | VAL VAL-LIST
*/

struct RPGParser{
    int lookup;
    File file;
    
    def this(){}

	def void parse(string file,Runtime r){
		if(this.file!=null){
            this.file.close();
        }
        this.file.open(file);
        this.lookup = ' ';
        while(!check(-1)){
            this.statement(r);
        }
        this.file.close();
	}

    def void statement(Runtime r){
        while(!(this.check('<')||this.check('\n'))){
           r.addLable(value);
           this.match('>');
        }
        r.addInstructions(this.instruction());
    }

	def Instruction instruction(){
        auto funcname = this.value();
        match(':');
        auto args = this.args();
        return new Instruction(funcname,args);
    }

    def bool check(int c){
        if(this.lookup == c){
            readch();
            return true;
        }
        return false;
    }
    
    def string[] args(){
        auto arg = new string[20];
        int i = 0;
        while(!this.check('\n')){
            arg[i++] = this.value();
        }
        return arg;
    }
    
    def void readch(){
        this.lookup = file.readch();
    }
    
    def string value(){
        string ans;
        while(!this.check('\n')){
            ans += this.readch();
        }
        return ans;
    }
    
}

struct RPGRuntime:Runtime{
    RPGParser parser;
    
    def this(RPGParser parser){
        this.parser = parser;
    }
    
    def override void open(string filename){
        this.parser.parse(filename,this);
    }
}