/* test code */

import  "lib/file.xs";
import  "lib/system.xs";
import  "math/Math.xs";
import  "container/list.xs";

print("-----------------\n");
print("+   Test Begin  +\n");
print("-----------------\n");

def void println(string s){
    print(s + "\n");
    return;
}

print("--------------------------\n");
print("+   Test For List Union  +\n");
print("--------------------------\n");

{
    srand(time());
    /*test code for list union*/
    list a ;
    a.init();
    list b ;
    b.init();
    int size = 7;
    for(int i = 0;i < size ;i++){
        a.push_front(rand()%31);
        b.push_front(rand()%31);
    }
    print("33\n");
    print("a=");print_list(a);
    print("b=");print_list(b);
    print("a U b=");print_list(union_list(a,b));
    print("SORT\n");
    print("sort(a)=\n");print_list(qlsort(a));
    getchar();
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
    println("Test for dynamic array");
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

println("Hello world!");

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



{
    /*test code for list*/
    println("Test for list");
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
}


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