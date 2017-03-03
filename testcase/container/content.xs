struct Content{
	@string
	def virtual string toString();
}

struct HashContent:Content {
    def virtual int hash();
    def virtual bool equals(HashContent c);
}

struct IntContent : Content{
    int val;
    def this(int val){
        this.val = val;
    }
    
    def override string toString(){
        return (string)(this.val);
    }
	
	@int
	def int toInt(){
		return val;
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
	def this(string value){
		this.value = value;
	}

    def override int hash(){
		int len = strlen(this.value);
		int hash_code = 0;
		while(len-- > 0 ){
			hash_code *= 10;
			hash_code += this.value[len];
		}
		return hash_code;
	}

    def override string toString(){
		return this.value;
	}
    
    
    def override bool equals(HashContent c){
		return c == this || c instanceof StringHashContent && ((StringHashContent)c).value = this.value;
	}
}