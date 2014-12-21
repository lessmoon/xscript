loadfunc<extension>{
    bool PutChar(char c);
    bool print(string str);
    int  strlen(string len);
    int  time();
}

def int repeat_print(string s,int c){
    int i = 0;
    for(i = 0;i < c;i++)
        print(s);
    return 0;
}
