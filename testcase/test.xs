import  "lib/file.xs";
import  "lib/system.xs";
import  "math/Math.xs";
import  "math/Ratio.xs";
import  "parser/parser.xs";

loadfunc<extension.ui>{
    int openPad(int w,int h);
    int drawLine(int x1,int y1,int x2,int y2);
    int drawPoint(int x,int y);
    int addLine(int x1,int y1,int x2,int y2);
    int addPoint(int x,int y);
    int setBrushColor(int r,int g,int b);
    int paint();
    int closePad();
    int clearPad();
}

loadfunc<extension.system>{
    void sleep(int duration);
}

loadfunc<extension.math>{
    real sin(real theta);
}

import  "container/list.xs";

def void f2(int b);

def void f1(int a){
    println("f1:" + a);
    if(a > 10){
        return;
    }
    f2(a+1);
}

def void f2(int b){
    println("f2:" + b);
    if(b > 10){
        return;
    }
    f1(b+1);
}


struct llist{
    int size;
    def int getSize();
    def bool isEmpty(){
        return this.getSize() == 0;
    }
}

def int llist.getSize(){
    return this.size;
}

{
    int x = time();
    println("Random seed:" + x);
    srand(x);
}

{
    print("------------------\n");
    print("+   Test Begin   +\n");
    print("------------------\n");
}

struct shape{
    string name;
    def void init(string name){
        this.name = name;
    }

    @string
    def string toString(){
        return this.name;
    }
    
    def virtual void draw();
}

struct square : shape{
    int width;
    def void setWidth(int w){
        this.width = w;
    }
 
    def override void draw(){
        int w = this.width;
        for(int i = 0 ; i < w ;i++){
            for(int j = 0 ; j < w;j++){
                print("* ");
            }
            print("\n");
        }
    }
}

struct rectangle : square {
    int length;
    def void setLength(int l){
        this.length = l;
    }
    
    def override void draw(){
        int w = this.width;
        int l = this.length;
        for(int i = 0 ; i < w ;i++){
            for(int j = 0 ; j < l;j++){
                print("* ");
            }
            print("\n");
        }
    }
}

{
    println("Test for inheriting");
    square dd = new<square>;
    dd.init("square");
    rectangle hh = new<rectangle>;
    hh.init("rectangle");
    shape[] h = new<shape>(2);
    h[0] = dd;
    h[1] = hh;
    dd.setWidth(10);
    hh.setWidth(4);
    hh.setLength(15);
    
    for(int i = 0 ; i < sizeof h;i++){
        println("draw a " + h[i]);
        h[i].draw();
        println("" + h[i] + " is a shape:"  + ( h[i] instanceof shape ));
        println("" + h[i] + " is a square:"  + ( h[i] instanceof square ));
        println("" + h[i] + " is a rectangle:"  + ( h[i] instanceof rectangle ));
    }
}


{
    println("Test for parser");
    parser p = new<parser>;
    lexer l = new<lexer>;
    string s = "1+5*6-(9-90)";
    l.init(s);
    p.init(l);
    println(s + "=" + p.expr().getValue());
}


struct complex{
    real r;
    real img;
    @+
    def complex add(complex x){
        complex res = new<complex>;
        res.r = x.r + this.r;
        res.img = x.img + this.r;
        return res;
    }

    @>
    def bool isGreaterThan(complex c){
        if(this.r > c.r)
            return true;
        else
            return this.r == c.r && this.img > c.img;
    }

    @string
    def string toString(){
        return "" + this.r + "+" + this.img + "*i";
    }
}

{
    println("Test for operand overloading");
    complex c1 = new<complex>,c2= new<complex>;
    c1.r = 2;
    c1.img = 3;
    c2.r = 5;
    c2.img = 6;
    print("c1=");
    println((string)c1);
    print("c2=");
    println((string)c2);
    println("c1+c2 = " + (c1+c2));
    println("c1>c2 = " + (c1>c2));
}

{
    println("Test for built-in variable _args_");
    int h;
    for(int i =0 ; i < sizeof _args_ ; i++)
        println("_args_[" + i + "]:" + _args_[i]);
}

{
    println("Test for big int");
    bigint x = 225I;
    bigint y = 226I;
    println("x = " + x + ",y= " + y + " ");
    x = x*x*x*x*x*x*x*x*x*y;
    println( "x*x*x*x*x*x*x*x*x*y = " + x );
    println("Test for big real");
    bigreal Rx = 0.225R;
    bigreal Ry = 0.226R;
    println("x = " + Rx + ",y= " + Ry + " ");
    Rx = Rx*Rx*Rx*Rx*Rx*Rx*Rx*Rx*Rx*Ry;
    println( "x*x*x*x*x*x*x*x*x*y = " + Rx );
}

{
    string x = "DEF";
    println("Test for switch code");
    println("switch string");
    switch(x){
    case "def":
        println("Wrong!");
        break;
    case "DEF":
        println("Correct!");
        break;
    default:
        println("Wrong!");
    }
    println("switch int");
    int i = 23;
    switch(i){
    case 1:
    case 2:
        println("Wrong!");
        break;
    case 23:
        println("Correct!");
        break;
    default:
        println("Wrong!");
    }
    println("switch char");
    char c = 'a';
    switch(c){
    case 1:
    case 2:
        println("Wrong!");
        break;
    case 'a':
        println("Correct!");
        break;
    default:
        println("Wrong!");
    }
}

{
    println("test for ratio");
    Ratio x = new<Ratio>,d = new<Ratio>;
    x.init(52,564);
    d.init(25,55);
    println(x.toString() + "+" + d.toString() + "=" + (x+d) );
    println(x.toString() + "-" + d.toString() + "=" + (x-d) );
    println(x.toString() + "*" + d.toString() + "=" + (x*d) );
    println(x.toString() + "/" + d.toString() + "=" + (x/d) );
}

{
    println("Test for pre-declaration code");
    f1(0);
}

{
    /*test for classic code*/
    println("Test for classic code");
    println("hello world!");
}    


{
    println("Test for built-in variable");
    println("This code is in file " + _file_ + " at " + _line_ );
    println("Compiler version is " + _version_/100 + "." + _version_%100);
}

{
    println("Test for animated painting");
    openPad(600,480);
    setBrushColor(255,0,0);
    int x,y;
    for(real off = 0;off < 1;off += 0.01 ){
        for(real theta = 0; theta <= 3.14 * 2 + 0.01;theta += 0.01){
            x = theta * 100 + (-314 + 300);
            y =  - sin(theta + off) * 100 + 240;
            addPoint(x,y);
        }
        paint();
        sleep(56);
        clearPad();
    }
    closePad();
}

{
    println("Test for basic painting");
    
    setBrushColor(25,25,255);
    {
        real x,y;int a = 20,b = 20;
        real x_m,y_m;
        for(real t = -10.0; t < 0 ;t += 0.01){
            x = 20*(t+1/t)/2;
            x_m = -x;
            x += 300;
            x_m += 300;
            y = 20*(t-1/t)/2;
            y = -y;
            y_m = -y;
            y += 240;
            y_m += 240;
            if(x < 600 && y < 480 && x > 0 && y > 0){
                addPoint(x,y);
                addPoint(x,y_m);
                addPoint(x_m,y);
                addPoint(x_m,y_m);
            }
        }
    }
   
    /*draw a red heart*/
    setBrushColor(255,0,0);
    for (real y = 1.5; y > -1.5; y -= 0.01) {
        real min = 1.5;
        real max = -1.5;
        for (real x = -1.5; x < 1.5; x += 0.005) {
            real a = x * x + y * y - 1;
            if(a * a * a - x * x * y * y * y <= 0.0){
                if(min > x)
                    min = x;
                if(max < x)
                    max = x;
            } else {
                if(min <= max){
                    addLine(min*100 + 300  - 100,-y*100 + 240 - 80,max*100 + 300  - 100,-y*100 + 240 - 80);
                    min = 1.5;
                    max = -15;
                }
            }
        }
        if(min <= max){
            addLine(min*100 + 300  - 100,-y*100 + 240 - 80,max*100 + 300  - 100,-y*100 + 240 - 80);
        }
    }
    
    /*draw a green round*/
    setBrushColor(0,255,0);
    real r = 100.0;
    for (int y = -r; y < r; y ++ ) {
        int min = r;
        int max = -r;
        for (int x = -r; x < r; x ++) {
            int a = x * x + y * y ;
            if(a <= r * r ){
                if(min > x)
                    min = x;
                if(max < x)
                    max = x;
            } else {
                if(min <= max){
                    addLine(min + 300 + 100 ,-y + 240 + 100,max + 300 + 100,-y + 240 + 100);
                    min = r;
                    max = -r;
                }
            }
        }
        if(min <= max){
            addLine(min + 300 + 100 ,-y + 240 + 100,max + 300 + 100,-y + 240 + 100);
        }
    }
    
    
    setBrushColor(0,0,0);
    addLine(0,240,600,240);
    addLine(300,0,300,480);
    openPad(600,480);
    paint();
    
    getchar();
    getchar();
    closePad();
}

{
    /*test code for list union*/
    list a = create_list() ;
    //a.init();
    list b = create_list() ;
    /*b.init();*/
    int size = 7;
    for(int i = 0;i < size ;i++){
        push_back(a,rand()%31);
        push_back(b,rand()%31);
    }
    println("Test for struct");
    print("Generating Test Data\n");
    print("a=");println(list_toString(a));
    print("b=");println(list_toString(b));
    
    print("a U b=");println(list_toString(union_list(a,b)));
    print("SORT\n");
    print("sort(a)=\n");

    println(list_toString(qlsort(a,0)));
}

struct CORD{
    real x;
    real y;
    def void init(real x,real y){
        this.x = x;
        this.y = y;
    }

    def string toString(){
        return "("+this.x+","+this.y+")";
    }
}



{
    println("Test for member functions");
    srand(time());
    CORD o = new<CORD>;
    o.x = rand()%25;
    o.y = rand()%25;
    println(o.toString());
    o.init(rand()%25,rand()%25);
    println(o.toString());
}


/*NEEDTEST*/

{
    println("Test for dynamic array sizeof");
    println("int[244] arr;");
    println("arr = new<int>(25);");
    println("println(sizeof arr);");
    println("println(sizeof new<int>(25));");
    int[] arr;
    arr = new<int>(25);
    println(sizeof arr);
    println(sizeof new<int>(25));
}

{
    println("Test for dynamic array");
    int[] tmp;
    int[][] x;
    int[][] b;
    x = new<int[]>(30);
    b = x;
    //x[1] = tmp;
    println("b[2] = " + b[2]);
    println("b[1] = " + b[1]);
}


int[][] a;
int buffer = -1;
a = new<int[]>(100);
for(int i = 0;i< sizeof a;i++){
    a[i] =  new<int>(100); 
}
a[21][25] = 212;
int c = a[24][25];

def int bufgetchar(){
    if( buffer < 0 ){
        return getchar();
    } else {
        int tmp = buffer;
        buffer = -1;
        return tmp;
    }
}

def int readint(){
    int i = 0;
    int c = bufgetchar();
    while(c <= '9' && c >= '0'){
        i *= 10;
        i += c - '0';
        c = bufgetchar();
    }
    buffer = c;
    return i;
}

{
    print("-----------------\n");
    print("+Test For Input +\n");
    print("-----------------\n");
    
    println("Sample:1+2[ENTER]");
    
    int i = readint();
    int t = bufgetchar();
    int j = readint();
    print("" + i + " " + (char) t + " " + j + "=");
    if(t == '+'){
        print(i+j);
    } else if(t == '-'){
        print(i-j);
    } else if(t == '*'){
        print(i*j);
    } else if(t == '/'){
        print(i/j);
    } else {
        print("error");
    }
    print("\n");
    getchar();
    getchar();
}


/*
{
    /*test code for list*/
/*    println("Test for list");
    list l ;
    l.init();
    srand(time());
    int size = rand()%50;
    for(size;size > 0;size--){
        l.push_front(rand()%9);
    }
    println("Original array:");
    print_list(l);
    l = qlsort(l);
    println("Sorted array:");
    //print_list(l);
    //print( qlsort(l) + "\n" );
}*/


struct tree_node{
    tree_node left;
    tree_node right;
    int value;
}

def tree_node randomly_create_tree(){
    tree_node t = new<tree_node>;
    if(rand()%10 > 5)
        t.left  = randomly_create_tree();
    if(rand()%10 < 5)
        t.right = randomly_create_tree();
    t.value = rand()%1000;
    return t;
}

def int prettily_show_tree(tree_node tree,int level){
    if(tree != null){
        repeat_print("  |",level);
        print(tree.value);
        print("\n");
        prettily_show_tree( tree.left,level + 1 );
        prettily_show_tree( tree.right,level + 1 );
    }
    return level;
}

{
    srand(time());
    tree_node tree ;
    tree = randomly_create_tree();
    prettily_show_tree(tree,0);
}


def string toString( char[] c_str ){
    string r = "";
    int i = 0;
    while(c_str[i] != '\0'){
        r += c_str[i++];
    }
    return r;
}

def int[][] mult( int[][] a,int[][] b){
    string r = "";
    int[][] res = new <int[]>(10);
    
    int i = 0;
    int sum = 0;
    int j = 0;
    int k = 0;
    for(i = 0 ; i < 10 ;i++){    
        res[i] = new<int>(10);
        for(j = 0 ; j < 10;j++){
            sum = 0;
            for(k = 0;k<10;k++){
                sum += a[i][k] * b[k][j];
            }
            res[i][j] = sum;
        }
    }
    return res;
}


def int printarray(int[] arr,int len){
    int i = 0;
    print("[");
    for(i;i < len;i++){
        print(" " + arr[i]);
    }
    print(" ]\n");
    return len;
}

{
  int[] arr ;
  int i = 0;
  int seed = time();
  print("seed = " + seed + "\n");
  srand( seed );
  int len = rand()%200;
  arr = new<int>(len);
  for(i = 0 ; i < len;i++){
    arr[i] = rand()%len;
  }
  printarray(arr,len);
  qsort(arr,0,len-1);
  printarray(arr,len);


  i = 0;
  string test = "i am a test value";
  char[] c_str = new<char>(100);

  int[][] a = new<int[]>(10);
  int[][] b = new<int[]>(10);
  int[][] c;
  
  for(i ;i < 10;i++){
    a[i] = new<int>(10);
    b[i] = new<int>(10);
    int j = 0;
    for(i ;j < 10;j++){
      a[i][j] = i;
      b[i][j] = j;
    }
  }

  c = mult(a,b);
  for(i = 0 ;i < 10;i++){
    int j = 0;
    print("[");
    for(i ;j < 10;j++){
        print(" ");
        print(c[i][j]);
    }
    print(" ]\n");
  }

  for(i = 0;i < strlen(test);i++){
    c_str[i] = test[i];
  }
  c_str[i] = '\0';
  string[][] nums = new<string[]>(10);
  print(toString(c_str));
  print("\n");
  
  for(i = 0 ;i < 10;i++){
    nums[i] = new<string>(20);
    for(int j = 0 ;j < 20;j++){
      nums[i][j] = (string)i + "," + j;
    }
  }

  
  for(i = 0 ;i < 10;i++){
    int j = 0;
    for(i ;j < 20;j++){
        print("(" + nums[i][j] + ")");
    }
    print("\n");
  }
  print("\n");
}

{
    println("Test for file write");
    int fid= open("t.txt");
    println("write to file " + "t.txt");
    
    for (char y = 'a'; y < 'z'; y++) {
        writech(fid,y);
        int i = y;
        string x = ((string)i);
        writech(fid,':');
        for(int i = 0 ; i < strlen(x);i++)
            writech(fid,x[i]);
        writech(fid,'\r');
        writech(fid,'\n');
    }
    close(fid);
}

def int max(int a,int b){
    return a>b?a:b;
}

def string trim(string str){
    string result = "";
    int len = strlen(str);
    int i = 0,j = len - 1;
    while(str[i] == ' ') i++;
    while(str[j] == ' ') j--;
    for(i;i <= j;i++){
       result += str[i];
    }
    return result;
}

def bool is_prime(int n){
    if(n == 1)
        return false;
    int i = 2;
    for(i;i * i <= n;i++ ){
        if(n%i == 0){
            return false;
        }
    }
    return true;
}

def real sqrt(real n){
    real a = n/2;
    int i = 0;
    for(i;i < 20;i++){
        a = (n/a+a)/2;
    }
    return a;
}

def int string2int(string s){
    int i = 0;
    int v = 0;
    int len = strlen(s);
    for(i;i < len;i++){
        v *= 10;
        v += (s[i] - '0');
    }
    return v;
}

{
    println("Test for user-define function string2int and sqrt");
    println((string)string2int("2556478"));
    print("sqrt(3)=" + sqrt(3) + "\n");
}

def string gestring(string str,char c,int i){
     string result = "";
     while(i < strlen(str)){
          if(str[i] >= c)
              result += str[i];
          i++;
     }
     return result;
}

def string lsstring(string str,char c,int i){
     int l = strlen(str);
     string result = "";
     while(i < l){
          if(str[i] < c)
              result += str[i];
          i++;
     }
     return result;
}

def string quicksort(string str){
    return strlen(str) <= 1?str:quicksort(lsstring(str,str[0],1)) + str[0] + quicksort(gestring(str,str[0],1));
}

{
    println("Test for string quicksort and trim function");
    string a = "    32145547852248562244    ";
    print("quicksort(\"" + a + "\") = \""+ quicksort(a) + "\"\n");
    print("trim( \"" + a +"\") = \"" + trim(a) + "\"\n");
}
print("-----------------\n");
print("+   Test End    +\n");
print("-----------------\n");