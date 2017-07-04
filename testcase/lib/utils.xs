native<extension.util>{
    "StringBufferX":struct StringBufferX{
        def this();
        def StringBufferX append(string str);
        def StringBufferX appendCharacter(char c);
        def StringBufferX delete(int beg,int end);
        def StringBufferX insert(int beg,string str);
        def StringBufferX reverse();
        def StringBufferX setCharAt(int index,char c);
        def void reserve(int size);
        def string toString();
    };
}

struct StringBuffer:StringBufferX{
    def this(){
        super();
    }
    
    def StringBufferX repeatCharacter(char c, int count){
        while(count-->0){
            this.append(c);
        }
    }
}

native<extension.util>{
    int  strlen(string len);
}

def bool isDigit(char c){
    return c>='0'&&c<='9';
}

def bool isEndLine(char c){
    return c=='\r'||c=='\n';
}

def bool isBlank(char c){
    return c==' '||isEndLine(c);
}

def bool isAlphaBeta(char c){
    return (c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A');
}

import"system.xs";
def bigint parseBigInt(string str){
    bigint num = 0;
    int len = strlen(str);
    bool is_minus = false;
    
    for(int i = 0 ; i < len;i++){
        if(isDigit(str[i])){
            num *= 10;
            num += str[i] - '0';
        } else if( str[i] == '-'){
            is_minus = !is_minus;
            continue;
        } else {
            break;
        }
    }    
    return is_minus?-num:num;
}

def int parseInt(string str){
    int num = 0;
    int len = strlen(str);
    bool is_minus = false;
    
    for(int i = 0 ; i < len;i++){
        if(isDigit(str[i])){
            num *= 10;
            num += str[i] - '0';
        } else if( str[i] == '-'){
            is_minus = !is_minus;
            continue;
        } else {
            break;
        }
    }
    return is_minus?-num:num;
}