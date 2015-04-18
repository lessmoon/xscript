import  "lib/file.xs";
import  "lib/system.xs";
import  "math/Math.xs";


loadfunc<extension>{
    int openPad(int w,int h);
    int drawLine(int x1,int y1,int x2,int y2);
    int drawPoint(int x,int y);
    int addLine(int x1,int y1,int x2,int y2);
    int addPoint(int x,int y);
    int setBrushColor(int r,int g,int b);
    int paint();
    int closePad();
    int clearPad();
    void sleep(int duration);
    real sin(real theta);
}

def void println(string s){
    print(s + "\n");
    return;
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

{
    println("Test for big int");
    bigint x = 225I;
    bigint y = 226I;
    println("x = " + x + ",y= " + y + " ");
    x = x*x*x*x*x*x*x*x*x*y;
    println( "x*x*x*x*x*x*x*x*x*y = " + x );
    println("Test for big real");
    bigreal Rx = 225R;
    bigreal Ry = 226R;
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
    println("Test for pre-declaration code");
    f1(0);
}

{
    /*test for classic code*/
    println("Test for classic code");
    println("hello world!");
}    


{
    println("Test for build-in variable");
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
        for (real x = -1.5; x < 1.5; x += 0.005) {
            real a = x * x + y * y - 1;
            if(a * a * a - x * x * y * y * y <= 0.0){
                addPoint(x*100 + 300  - 100,-y*100 + 240 - 80);
            }
        }
    }
    
    /*draw a green round*/
    setBrushColor(0,255,0);
    real r = 100.0;
    for (int y = r; y > -r; y -= 1) {
        for (int x = -r; x < 100; x += 1) {
            int a = x * x + y * y ;
            if(a <= r * r ){
                addPoint(x + 300 + 100 ,-y + 240 + 100);
            }
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
    //b.init();
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
//return 0;

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
    CORD o;
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
    int[244] arr;
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



/*FIXME*/
int[22][23] a;
int buffer = -1;
a[21][0] = 212;
int c = a[21][0];

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

tree_node null;
null = null.left;

def tree_node randomly_create_tree(){
    tree_node t;
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


def string toString( char[80] c_str ){
    string r = "";
    int i = 0;
    while(c_str[i] != '\0'){
        r += c_str[i++];
    }
    return r;
}

def int[10][10] mult( int[10][10] a,int[10][10] b){
    string r = "";
    int[10][10] res;
    int i = 0;
    int sum = 0;
    int j = 0;
    int k = 0;
    for(i = 0 ; i < 10 ;i++){        
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


def int printarray(int[2] arr,int len){
    int i = 0;
    print("[");
    for(i;i < len;i++){
        print(" " + arr[i]);
    }
    print(" ]\n");
    return len;
}

{
  int[1000] arr;
  int i = 0;
  int seed = time();
  print("seed = " + seed + "\n");
  srand( seed );
  int len = rand()%200;
  for(i = 0 ; i < len;i++){
    arr[i] = rand()%len;
  }
  printarray(arr,len);
  qsort(arr,0,len-1);
  printarray(arr,len);


  i = 0;
  string test = "i am a test value";
  char[80] c_str;

  int[10][10] a;
  int[10][10] b;
  int[10][10] c;
  for(i ;i < 10;i++){
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
  string[10][20] nums;
  print(toString(c_str));
  print("\n");
  
  for(i = 0 ;i < 10;i++){
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
  /*
  int fid= open("t.txt");
   
  real y = 1.5;
  for (y; y > -1.5; y -= 0.1) {
    real x = -1.5;
    for (x; x < 1.5; x += 0.05) {
        real a = x * x + y * y - 1;
        writech(fid,a * a * a - x * x * y * y * y <= 0.0 ? '*' : ' ');
    }
    writech(fid,'\r');
    writech(fid,'\n');
  }
   close(fid);
  */
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
    println((string)string2int("2556478"));
    print("" + sqrt(3) + "\n");
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
    string a = "    32145547852248562244    ";
    print("quicksort(\"" + a + "\") = \""+ quicksort(a) + "\"\n");
    print("trim( \"" + a +"\") = \"" + trim(a) + "\"\n");
}
print("-----------------\n");
print("+   Test End    +\n");
print("-----------------\n");