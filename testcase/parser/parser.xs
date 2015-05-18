
struct Expr {
    @real
    def virtual real getValue();
}

struct Arith:Expr{
    Expr e1;
    Expr e2;
    def void init(Expr e1,Expr e2){
        this.e1 = e1;
        this.e2 = e2;
    }
}

struct Add:Arith{
    def override real getValue(){
        return this.e1.getValue()+this.e2.getValue();
    }
}

struct Sub:Arith{
    def override real getValue(){
        return this.e1.getValue()-this.e2.getValue();
    } 
}

struct Mult:Arith{
    def override real getValue(){
        return this.e1.getValue()*this.e2.getValue();
    } 
}

struct Div:Arith{
    def override real getValue(){
        return this.e1.getValue()/this.e2.getValue();
    } 
}

struct Const:Expr{
    real value;
    def void setValue(int v){
        this.value = v;
    }

    def override real getValue(){
        return this.value;
    }
}

struct Var:Const{}

struct Token{
    int tag;

    @string
    def virtual string toString(){
        return (string)this.tag;
    }
    
    def void setTag(int d){
        this.tag = d;
    }
}

struct Num:Token{
    real value;

    def override string toString(){
        return (string)this.value;
    }

    def void init(real value){
        this.value = value;
        this.setTag(256);
    }
}

def bool isDigital(char c){
    return c <= '9' && c >= '0';
}

struct lexer{
    int no;
    char peek;
    string poly;
    def void init(string poly){
        this.poly = poly;
        this.peek = ' ';
        this.no = 0;
    }

    def void readch(){

        if(this.no >= strlen(this.poly)){
            this.peek = 0;
        } else { 
            this.peek = this.poly[this.no];
            this.no++;
        }
    }

    def Token scan(){
        Token t = new<Token>;

        while(this.peek == ' '){
            this.readch();
        }
        char c = this.peek;
        if(isDigital(c)){
            int i = 0;
            do{
                i *= 10;
                i += (c-'0');
                this.readch();
                c = this.peek;
            }while(isDigital(c));
            Num n = new<Num> ;
            n.init(i);
            return n;
        }
        t.setTag(c);
        this.peek = ' ';
        return t;
   
    }
}

struct parser{
    lexer lex;
    Token look;
    Var  xVar;

    def void init(lexer l){
        this.lex = l;
        this.xVar = new <Var>;
    }

    def Var getVar(){
        return this. xVar;
    }
    
    def void next(){
        this.look = this.lex.scan();
    }

    def Expr term();
    def Expr mult();
    def Expr add();
    def Expr expr(){
        this.next();
        return this.add();
    }
}

def Expr parser.term(){
    switch(this.look.tag){
    case '(':
        this.next();
        Expr e = this.add();
        if(this.look.tag != ')'){
            println("`(' mismatched");
        } else {
            this.next();
        }
        return e;
    case 256:
        Const e = new<Const>;
        e.setValue(((Num)this.look).value);
        this.next();
        return e;
    case 'x':case 'X':
        this.next();
        return this.getVar();
    default:
        println("Unknown token `" + this.look + "' found");
    }
}

def Expr parser.mult(){
    Expr e = this.term();
    while(this.look.tag == '*' || this.look.tag == '/'){
        if(this.look.tag == '*'){
            this.next();
            Mult m = new<Mult>;
            m.init(e,this.term());
            e = m;
        } else {
            this.next();
            Div d = new<Div>;
            d.init(e,this.term());
            e = d;
        }
    }
    return e;
}

def Expr parser.add(){
    Expr e = this.mult();
    while(this.look.tag == '+' || this.look.tag == '-'){
        if(this.look.tag == '+'){
            this.next();
            Add a= new<Add>;
            a.init(e,this.mult());
            e = a;
        } else {
            this.next();
            Sub s= new<Sub>;
            s.init(e,this.mult());
            e = s;
        }
    }
    return e;
}

