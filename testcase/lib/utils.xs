native<extension.util>{
    "StringBufferX":struct StringBuffer{
        def this();
        def StringBuffer append(string str);
        def StringBuffer appendCharacter(char c);
        def StringBuffer delete(int beg,int end);
        def StringBuffer insert(int beg,string str);
        def StringBuffer reverse();
        def StringBuffer setCharAt(int index,char c);
        def void reserve(int size);
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