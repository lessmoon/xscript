def int do_gcd(int a,int b){
    return (b>0)?do_gcd(b,a%b):a;
}

def int gcd(int a,int b){
    a = a>0?a:-a;
    b = b>0?b:-b;
    return do_gcd(a,b);
}

struct Ratio{
    int num;
    int den;

    def Ratio reduce(){
        int f = gcd(this.num,this.den);
        this.num /= f;
        this.den /= f;
        return this;
    }

    def void init(int num,int den){
        this.num = num;
        this.den = den;
        this.reduce();
    }

    @+
    def Ratio add(Ratio r){
        Ratio tmp = new<Ratio>;
        tmp.num = r.den * this.num +  this.den* r.num;
        tmp.den = r.den * this.den;
        return tmp.reduce();
    }

    @-
    def Ratio sub(Ratio r){
        Ratio tmp= new<Ratio>;
        tmp.num = r.den * this.num -  this.den* r.num;
        tmp.den = r.den * this.den;
        return tmp.reduce();
    }

    @*
    def Ratio mult(Ratio r){
        Ratio tmp = new<Ratio>;
        tmp.num = r.num * this.num ;
        tmp.den = r.den * this.den;
        return tmp.reduce();
    }

    @/
    def Ratio div(Ratio r){
        Ratio tmp = new<Ratio>;
        tmp.num = this.num * r.den;
        tmp.den = r.num * this.den;
        return tmp.reduce();
    }

    //@==
    def bool equals(Ratio r){
        r.reduce();
        this.reduce();
        return this.num == r.num && this.den == r.den;
    }    
    
    @string
    def string toString(){
        return "" + this.num + "/" + this.den;
    }
}