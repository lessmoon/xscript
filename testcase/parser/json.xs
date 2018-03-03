import "../container/hashmap.xs";
import "../container/darray.xs";

struct CharInputStream{
    def default virtual int next();
}

struct JSON:Content{
    def virtual string alignedString(int align);

    def override string toString(){
        return this.alignedString(0);
    }

    def virtual bool isNull(){
        return false;
    }
    
    def virtual bool isString(){
        return false;
    }
    
    def virtual bool isBoolean(){
        return false;
    }
    
    def virtual bool isArray(){
        return false;
    }
    
    def virtual bool isObject(){
        return false;
    }

    def virtual bool isNumber(){
        return true;
    }

}

/*
 * "somestr"
 */
struct JSONString:JSON{
    string value;

    def this(string value){
        this.value = value;
    }

    def void setValue(string value){
        this.value = value;
    }

    def string getValue(){
        return this.value;
    }

    def override string alignedString(int align){
        return "\""+this.value+"\"";
    }
    
    def override bool isString(){
        return true;
    }
}

/*
 * {
 *   "name":"",
 *   "age":""
 *  }
 */
struct JSONObject:JSON{
    HashMap map;

    def this(){
        this.map = new HashMap();
    }

    def void insert(string key, JSON o){
        this.map.set(new StringHashContent(key), o);
    }
    
    def JSON get(string key){
        return (JSON)this.map.get(new StringHashContent(key)).value;
    }

    def Iterator iterator(){
        return this.map.iterator();
    }

    def int size(){
        return this.map.size();
    }

    def override bool isObject(){
        return true;
    }

    def override string alignedString(int align){
        StringBuffer b = new StringBuffer();
        b.append("{");
        bool[] is_begin = {true};
        this.map.iterator().forEachRemained(new Consumer^v->{
            const auto x = (HashPair)v;
            const auto o = (JSON) x.value;
            b.append(is_begin[0]?"\n":",\n");
            b.repeatCharacter(' ', (align+1)*4);
            b.append("\"").append(x.key.toString()).append("\"").append(": ").append(o.alignedString(align+1));
            is_begin[0] = false;
        });
        b.append("\n");
        b.repeatCharacter(' ', align*4);
        b.append("}");
        return b.toString();
    }
}
/*
 * [1,1,2,3,4,5,6,{"name":"c"}]
 */
struct JSONArray:JSON{
    DynamicArray array;

    def this(int capacity){
        this.array = new DynamicArray(capacity);
    }

    def void add(JSON element){
        this.array.add(element);
    }

    def JSON get(int id){
        return (JSON) this.array.get(id);
    }

    def int size(){
        return this.array.size();
    }
    
    def override string alignedString(int align){
        StringBuffer b = new StringBuffer();
        b.append("[");
        bool[] is_begin= {false};
        this.array.iterator().forEachRemained(new Consumer^v->{
            auto h = (JSON)v;
            if (is_begin[0]) {
                b.append(", ");
            }
            b.append(h.alignedString(align+1));
            is_begin[0] = true;
        });
        b.append("]");
        return b.toString();
    }
}

struct JSONNumber:JSON{
    real value;

    def this(real value){
        this.value = value;
    }

    def real getValue(){
        return this.value;
    }

    def void setValue(real value){
        this.value = value;
    }

    def override bool isNumber(){
        return true;
    }

    def override string alignedString(int align){
        return this.value;
    }
}

struct JSONBoolean:JSON{
    bool value;
    
    def this(bool value){
        this.value = value;
    }

    def bool getValue(){
        return this.value;
    }

    def bool setValue(bool value){
        this.value = value;
    }

    def override bool isBoolean(){
        return true;
    }

    def override string alignedString(int align){
        return this.value;
    }
}

struct JSONToken:Content{
    int tag;
    def this(int tag){
        this.tag = tag;
    }

    def override string toString(){
        return (char)this.tag;
    }
}

const int   TAG_JSON_STR = 256, TAG_JSON_NUM = 257,
            TAG_JSON_TRUE = 258,TAG_JSON_FALSE = 259,
            TAG_JSON_NULL = 260;

struct JSONStrToken:JSONToken{
    string value;
    
    def this(string value){
        super(TAG_JSON_STR);
        this.value = value;
    }
    
    def override string toString(){
        return this.value;
    }
}

struct JSONNumToken:JSONToken{
    real value;
    
    def this(real value){
        super(TAG_JSON_NUM);
        this.value = value;
    }
    
    def override string toString(){
        return this.value;
    }
}
const auto  JSON_NULL_TOKEN = new JSONToken(TAG_JSON_NULL),
            JSON_TRUE_TOKEN = new JSONToken(TAG_JSON_TRUE),
            JSON_FALSE_TOKEN = new JSONToken(TAG_JSON_FALSE);

const JSON JSON_NULL = new JSON{
                            def override bool isNull(){
                                return true;
                            }
                            
                            def override string alignedString(int aligned){
                                return "null";
                            }
                        },
            JSON_TRUE = new JSONBoolean(true),
            JSON_FALSE = new JSONBoolean(false);

struct JSONLexer{
    int peek;
    CharInputStream cis;
    
    def this(CharInputStream cis){
        this.cis = cis;
        this.peek = ' ';
    }
    
    def void read(){
        this.peek = this.cis.next();
    }
    
    def JSONToken scan(){
        while(this.peek == ' '|| this.peek == '\r' ||this.peek == '\t'|| this.peek == '\n'){
            this.read();
        }

        if(this.peek == '\"'){//string
            StringBuffer sb = new StringBuffer();
            this.read();
            while(this.peek != '\"'){
                sb.appendCharacter(this.peek);
                this.read();
            }
            this.read();
            return new JSONStrToken(sb.toString());
        } else if(this.peek >= '0' && this.peek <= '9'){
            real x = this.peek - '0';
            this.read();
            while(this.peek >= '0' && this.peek <= '9'){
                x = x*10 + this.peek - '0';
                this.read();
            }
            real r = 0.1;
            if(this.peek == '.'){
                this.read();
                while(this.peek >= '0' && this.peek <= '9'){
                    x += r*(this.peek - '0');
                    r /= 10;
                    this.read();
                }
            }
            return new JSONNumToken(x);
        } else if(isAlphaBeta(this.peek)){
            StringBuffer b = new StringBuffer();
            do{
                b.appendCharacter(this.peek);
                this.read();
            }while(isAlphaBeta(this.peek));
            const auto str = b.toString();
            
            switch(str){
                case "true" :return JSON_TRUE_TOKEN;
                case "false":return JSON_FALSE_TOKEN;
                case "null" :return JSON_NULL_TOKEN;
                default     :return null;//error
            }
        } else {
            int tmp = this.peek;
            this.read();
            return new JSONToken(tmp);
        }
    }
}

struct JSONParser{
    JSONToken look;
    JSONLexer lex;

    def this(JSONLexer lex){
        this.lex = lex;
    }

    def void next(){
        this.look = this.lex.scan();
    }

    def bool moveIf(int tag){
        if(this.look.tag == tag){
            this.next();
            return true;
        }
        return false;
    }

    def JSON element();
    
    def JSON parse(){
        this.next();
        return this.element();
    }

    def bool moveIfNot(int tag){
        if(this.look.tag != tag){
            this.next();
            return true;
        }
        return false;
    }

    def JSONArray array(){
        this.moveIf('[');
        auto array = new JSONArray(10);
        if(']' != this.look.tag){
            do{
                array.add(this.element());
            }while(this.moveIf(','));
        }
        this.moveIf(']');
        
        return array;
    }

    def JSONObject object(){
        this.moveIf('{');
        auto object = new JSONObject();
        if('}' != this.look.tag){
            do{
                auto key = ((JSONStrToken)this.look).value;
                this.next();
                this.moveIf(':');
                object.insert(key,this.element());
            }while(this.moveIf(','));
        }
        this.moveIf('}');
        return object;
    }
}

def JSON JSONParser.element(){
    switch (this.look.tag) {
    case TAG_JSON_NUM:
        auto tmp = this.look;
        this.next();
        return new JSONNumber(((JSONNumToken)tmp).value);
    case TAG_JSON_STR:
        auto tmp = this.look;
        this.next();
        return new JSONString(((JSONStrToken)tmp).value);
    case '[':
        return this.array();
    case '{':
        return this.object();
    case TAG_JSON_NULL:
        this.next();
        return JSON_NULL;
    case TAG_JSON_TRUE:
        this.next();
        return JSON_TRUE;
    case TAG_JSON_FALSE:
        breakWhen(true);
        this.next();
        return JSON_FALSE;
    }
}

def JSON parseJSON(string s){
    return new JSONParser(new JSONLexer(new CharInputStream(s){
        string value;
        int idx;
        
        def this(string val){
            this.value = val;
            this.idx = 0;
        }
        
        def override int next(){
            if(this.idx < strlen(this.value)){
                return this.value[this.idx++];
            }
            return -1;
        }
    })).parse();
}