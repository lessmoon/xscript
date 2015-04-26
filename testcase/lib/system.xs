loadfunc<extension.system>{
    bool PutChar(char c);
    bool print(string str);
    int  time();
    int  getchar();
}

loadfunc<extension.util>{
    int  strlen(string len);
}

def int repeat_print(string s,int c){
    int i = 0;
    for(i = 0;i < c;i++)
        print(s);
    return 0;
}
