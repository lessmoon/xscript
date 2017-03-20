struct Content{
    @string
    def virtual string toString();
    
    @bool
    def virtual bool toBool(){
        return true;
    }
}

struct Comparable:Content{
    @<
    def virtual bool less(Comparable a);
    
    @>
    def bool more(Comparable a){
        return a.less(this);
    }
}

struct BoolContent:Content{
    bool value;
    def this(bool value){
        this.value = value;
    }

    @string
    def override string toString(){
        return this.value;
    }
    
    def override bool toBool(){
        return this.value;
    }
}

struct HashContent:Content {
    def virtual int hash();
    def virtual bool equals(HashContent c);
}

struct IntContent : Comparable{
    int val;
    def this(int val){
        this.val = val;
    }
    
    def override string toString(){
        return (string)(this.val);
    }
    
    def override bool less(Comparable a){
        return this.val < ((IntContent)a).val;
    }
    
    @int
    def int toInt(){
        return this.val;
    }
}

struct StringContent : Content{
    string val;
    def this(string val){
        this.val = val;
    }
    
    def override string toString(){
        return this.val;
    }
}

struct StringHashContent:HashContent{
    string value;
    int hash;
    bool hashed;
    
    def this(string value){
        this.value = value;
        this.hashed = false;
    }

    def override int hash(){
        if(this.hashed){
            return this.hash;
        }
        
        int len = strlen(this.value);
        int hash_code = 0;
        while(len-- > 0 ){
            hash_code *= 10;
            hash_code += this.value[len];
        }
        if( hash_code < 0 )
            hash_code = - hash_code;
        return this.hash = hash_code;
    }

    def override string toString(){
        return this.value;
    }
    
    
    def override bool equals(HashContent c){
        return c == this || c instanceof StringHashContent && ((StringHashContent)c).value == this.value;
    }
}

struct Iterator{
    def virtual void next(){}
    def virtual bool hasNext(){return false;}
    @Content
    def virtual Content getValue();
}