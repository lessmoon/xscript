native<extension.util>{
    "StringBufferX":struct StringBuffer{
        def this();
        def void append(string str);
        def void appendCharacter(char c);
        def void delete(int beg,int end);
        def void insert(int beg,string str);
        def StringBuffer reverse();
        def void setCharAt(int index,char c);
        def string toString();
    };
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