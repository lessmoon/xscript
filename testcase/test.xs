import  "lib/file.xs";
import  "lib/system.xs";
import  "math/Math.xs";
import  "math/Ratio.xs";
import  "parser/parser.xs";
import  "container/darray.xs";
import  "container/hashmap.xs";
import  "container/list.xs";


native<extension.system>{
    "sleep":void sleep(int duration);
}


struct Runnable {
    def virtual void run();
}

native<extension.predefined>{
    "SimpleThread":struct Thread{
        def this(Runnable);
        def void start();
    };
}

bool isOdd = true;

struct PrintNumber : Runnable {
    int index;

    def this(int beg) {
        this.index = beg;
    }
    
    def override void run(){
        
        for(int i = this.index;i<100;i+=2){
            while((i%2==0)==isOdd){}
            isOdd=!isOdd;
            println(i);
        }
    }
}

{
    Thread t1 = new Thread(new PrintNumber(2));
    Thread t2 = new Thread(new PrintNumber(1));
    t1.start();
    sleep(1);
    t2.start();
    sleep(60);
    
    
}


native<extension.predefined>{
    "TestStruct":struct NTVSTRT{
        string id;
        def virtual string getId();
    };
}

struct MyStruct : NTVSTRT{
    def override string getId(){
        return "hello," + this.id ;
    }

    def virtual void setId(string id){
        this.id = id;
    }
}

native<extension.system>{
    "sleep":void sleep(int duration);
}
import  "ui/paintpad.xs";

native<extension.ui>{
    "openPad":int openPadWithName(int w,int h,string name);
    int drawLine(int x1,int y1,int x2,int y2);
    int drawPoint(int x,int y);
    int addLine(int x1,int y1,int x2,int y2);
    int addPoint(int x,int y);
    int setBrushColor(int r,int g,int b);
    int paint();
    int closePad();
    int clearPad();
}


native<extension.math>{
    real sin(real theta);
	real cos(real theta);
    "SetSeed":void srand(int seed);
    "Random":int rand();
}


struct StringHashContent : HashContent {
    string val;
    def this(string val){
        this.val = val;
    }

    def override int hash(){
        int v = 0;
        int len   = strlen(this.val) ;
        for(int i = 0;i < len;i++){
            v += this.val[i];
        }
        return v;
    }

    def override string toString(){
        return this.val;
    }

    def override bool equals(HashContent x){
        return this.val == ((StringHashContent)x).val;
    }
}


struct IntValueContent : ValueContent{
    int val;
    def this(int val){
        this.val = val;
    }
    
    def override string toString(){
        return (string)(this.val);
    }
}

{
   HashMap hm = new HashMap();
   for(int i = 0; i < 20;i++){
        hm.set(new StringHashContent(""+rand()),new IntValueContent(rand()));
        println("" + i + "\n" + hm);
   }
}




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

real PI = 3.141592654;

def void drawHand(int v,real theta,int len,int r,int g,int b){
	real arctheta = theta * 2 * PI;
	int x = cos(arctheta) * len;
	int y = sin(arctheta) * len;
	setBrushColor(r,g,b);
	addLine(150,150,150+x,y+150);
}

def void drawClock(Time t){
	for(int i = 1 ; i < 13;i++){
		real arctheta = ((real)i-3)/6 * PI;
		int x = cos(arctheta) * 140;
		int y = sin(arctheta) * 140;
		addString("" + i,150 + x,150+y);
	}

	drawHand(t.hour,((real)t.hour-3) / 12,70,0,0,255);
	drawHand(t.minute,((real)t.minute-15) / 60,110,0,255,0);
	drawHand(t.second,((real)t.second-15) / 60,130,255,0,0);
}

{
	MyTime t = new MyTime;
	println(t);
	
	openPadWithName(300,300,"ClockInXScript");
	while(true){
		println(t);
		getTime(t);
		clearPad();
		drawClock(t);
		paint();
		sleep(500);
	}
	
}

{
    println("Test for array initial list");
    int[][] x = {{23,52},{25,64+78},{54},{}};
    for(int i = 0 ; i < sizeof x;i++){
		for(int j = 0 ; j < sizeof x[i];j++){
			print(" " + x[i][j]);
		}
		print("\n");
	}
}

{
    println("Test for predefined struct");
    MyStruct s = new MyStruct;
    s.setId(25);
    println(s.getId());
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
    square dd = new square;
    dd.init("square");
    rectangle hh = new rectangle;
    hh.init("rectangle");
    shape[] h = new shape[2];
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

def string readLine(){
    string l = "";
    char c;
    while((c = getchar()) != '\n' ){
        l+=c;
    }
    return l;
}

def void drawRectangle(int x,int y,int w,int h){
    addLine(x,y,x+w,y);
    addLine(x,y,x,y+h);
    addLine(x,y+h,x+w,y+h);
    addLine(x+w,y,x+w,y+h);
    
}
    int WIDTH = 50;
    int HEIGHT = 50;
    int TILE_WIDTH = 10;
    int CHESS_WIDTH = 8;
/*CHESS PLAYING*/
def void drawChessPad(int x,int y)
{
    clearPad();
    setBrushColor(0,0,0);
    for(int i = 0;i <= WIDTH;i++){
        //println("x " + i + ":" + TILE_WIDTH*i+","+HEIGHT*TILE_WIDTH);
        addLine(TILE_WIDTH*i,0,TILE_WIDTH*i,HEIGHT*TILE_WIDTH);
    }
    for(int i = 0;i <= HEIGHT;i++){
        //println("y " + i + ":" + TILE_WIDTH*i+","+WIDTH*TILE_WIDTH);
        addLine(0,TILE_WIDTH*i,WIDTH*TILE_WIDTH,TILE_WIDTH*i);
    }
    setBrushColor(255,0,0);
    int co = (TILE_WIDTH - CHESS_WIDTH)/2;
    drawRectangle(x*TILE_WIDTH+co,y*TILE_WIDTH+co,CHESS_WIDTH,CHESS_WIDTH);

    paint();
}

struct KeyboardAdapter: EventCallback{
    int x;
    int y;
    def override bool callback(int id){
        switch(id){
        case 'W':case 'w':
            this.y--;
            break;
        case 'S':case 's':
            this.y++;
            break;
        case 'A':case 'a':
            this.x--;
            break;
        case 'D':case 'd':
            this.x++;
            break;
        case ' ':case 'e':
            return false;
        }
        drawChessPad(this.x,this.y);
        return true;
    }
}

struct MouseAdapter:MouseEventCallback{
    int count;
    def override bool callback(int x,int y){
        println("" + x + "," + y);
        int newX = x/TILE_WIDTH;
        int newY = y/TILE_WIDTH;
        drawChessPad(newX,newY);
        return --this.count != 0;
    }
} 

{
    println("Test for chess pad");
    openPadWithName(WIDTH*TILE_WIDTH+1,HEIGHT*TILE_WIDTH+1,"Chess");
    KeyboardAdapter kba = new KeyboardAdapter;
    kba.x = WIDTH/2;
    kba.y = HEIGHT/2;
    drawChessPad(kba.x,kba.y);
    if(!loopForKeyboard(kba)){
        println("error occurred");
    }
    
    println("End test for the keyboard");
    println("Test for mouse adapter");
    MouseAdapter ma = new MouseAdapter;
    ma.count = 10;
    if(!loopForMouse(ma)){
        println("err occured");
    }
    println("end for the mouse");
    clearPad();
    closePad();
}

/*CELL SIMULATION*/
def void drawWorld(bool[][] worldmap)
{
    clearPad();
    setBrushColor(0,0,0);
    //println("Line " + _line_ + ":" + TILE_WIDTH);
    for(int i = 0;i <= WIDTH;i++){
        //println("x " + i + ":" + TILE_WIDTH*i+","+HEIGHT*TILE_WIDTH);
        addLine(TILE_WIDTH*i,0,TILE_WIDTH*i,HEIGHT*TILE_WIDTH);
    }
    for(int i = 0;i <= HEIGHT;i++){
        //println("y " + i + ":" + TILE_WIDTH*i+","+WIDTH*TILE_WIDTH);
        addLine(0,TILE_WIDTH*i,WIDTH*TILE_WIDTH,TILE_WIDTH*i);
    }
    setBrushColor(255,0,0);
    int co = (TILE_WIDTH - CHESS_WIDTH)/2;
    for(int x = 0;x < WIDTH;x++){
        for(int y = 0;y < HEIGHT;y++){
            if(worldmap[x][y])
                drawRectangle(x*TILE_WIDTH+co,y*TILE_WIDTH+co,CHESS_WIDTH,CHESS_WIDTH);
        }
    }
    paint();
}

def bool isAlive(bool[][] src,int x,int y){
    int counter = 0;
    if(x > 0){
        if(src[x-1][y]){
            counter++;
        }
        if(y < HEIGHT - 1){
            if(src[x-1][y+1]){
                counter++;
            }
        }
    }
    if(x < WIDTH - 1){
        if(src[x+1][y]){
            counter++;
        }
        if(y > 0){
            if(src[x+1][y-1]){
                counter++;
            }
        }
    }
    if(y > 0){
        if(src[x][y-1]){
            counter++;
        }
        if(x > 0){
            if(src[x-1][y-1]){
                counter++;
            }
        }
    }
    if(y < HEIGHT - 1){
        if(src[x][y+1]){
            counter++;
        }
        if(x < WIDTH - 1){
            if(src[x+1][y+1]){
                counter++;
            }
        }
    }
    
    switch(counter){
    case 2:
        return src[x][y];
    case 3:
        return true;
    default:
        return false;
    }
}

def void calMap(bool[][] src,bool[][] tar){
    for(int x = 0;x < WIDTH;x++){
        for(int y = 0;y < HEIGHT;y++){
            tar[x][y] = isAlive(src,x,y);
        }
    }
}

def void GameOfLife(int max)
{
    //println("Line " + _line_ + ":" + TILE_WIDTH);
    openPadWithName(WIDTH*TILE_WIDTH+1,HEIGHT*TILE_WIDTH+1,"GameOfLife");
    //println("Line " + _line_ + ":" + TILE_WIDTH);
    bool[][] world,world1 = new bool[][WIDTH],
             world2 = new bool[][WIDTH];
    for(int i = 0 ; i < WIDTH;i++){
        world1[i] = new bool[HEIGHT];
        world2[i] = new bool[HEIGHT];
    }
    srand(time());
    int sum = rand()%(WIDTH*HEIGHT);
    
    for(int i = 0;i<sum;i++){
        int x,y;
        do{
            x = rand()%WIDTH;
            y = rand()%HEIGHT;
        }while(world1[x][y]);
        world1[x][y] = true;
    }
    drawWorld(world1);
    sleep(200);
    //println("Line " + _line_ + ":" + TILE_WIDTH);
    while(max-- > 0){
        calMap(world1,world2);
        world = world2;
        world2 = world1;
        world1 = world;
        drawWorld(world);
        sleep(200);
    }
    clearPad();
    closePad();
}

{
    println("Test for Game of Life");
    GameOfLife(10);
}

{
    println("Test for parser");
    parser p = new parser;
    lexer l = new lexer;
    //print("Enter the function you want to draw:f(x)=");
    string s = "(x/10-10)*(x/10-20)";
    l.init(s);
    p.init(l);
    Var v = p.getVar();
    Expr e = p.expr();

    /*draw a red line*/
    int last_x = -10000;
    int last_y = -10000;
    setBrushColor(255,0,0);
    for (real x = 300; x > -300; x -= 1) {
        v.setValue(x);
        int y = e.getValue();
        addPoint(x + 300 ,-y + 240);
        if(last_x > -10000){
            addLine(last_x,last_y,x+300,-y+240);
        }
        last_x = x+300;
        last_y = -y+240;
    }
    println("drawing function line of f(x)=" + s);
    setBrushColor(0,0,0);
    addLine(0,240,600,240);
    addLine(300,0,300,480);
    openPad(600,480);
    paint();
    getchar();
    getchar();
    closePad();
}



struct complex{
    real r;
    real img;
    @+
    def complex add(complex x){
        complex res = new complex;
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
    complex c1 = new complex,c2= new complex;
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
    Ratio x = new Ratio,d = new Ratio;
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
    CORD o = new CORD;
    o.x = rand()%25;
    o.y = rand()%25;
    println(o.toString());
    o.init(rand()%25,rand()%25);
    println(o.toString());
}


/*NEEDTEST*/

{
    println("Test for dynamic array sizeof");
    println("int[] arr;");
    println("arr = new int[25];");
    println("println(sizeof arr);");
    println("println(sizeof new<int>(25));");
    int[] arr;
    arr = new int[25];
    println(sizeof arr);
    println(sizeof new int[25]);
}

{
    println("Test for dynamic array");
    int[] tmp;
    int[][] x;
    int[][] b;
    x = new int[][30];
    b = x;
    //x[1] = tmp;
    //println("b[2] = " + b[2]);
    //println("b[1] = " + b[1]);
}


int[][] a;
int buffer = -1;
a = new int[][100];
for(int i = 0;i< sizeof a;i++){
    a[i] =  new int[100]; 
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
    tree_node t = new tree_node;
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
    int[][] res = new int[][10];
    
    int i = 0;
    int sum = 0;
    int j = 0;
    int k = 0;
    for(i = 0 ; i < 10 ;i++){    
        res[i] = new int[10];
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
  arr = new int[len];
  for(i = 0 ; i < len;i++){
    arr[i] = rand()%len;
  }
  printarray(arr,len);
  qsort(arr,0,len-1);
  printarray(arr,len);

  
  i = 0;
  string test = "i am a test value";
  char[] c_str = new char[100];

  int[][] a = new int[][10];
  int[][] b = new int[][10];
  int[][] c;
  
  for(i ;i < 10;i++){
    a[i] = new int[10];
    b[i] = new int[10];
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
  string[][] nums = new string[][10];
  print(toString(c_str));
  print("\n");
  
  for(i = 0 ;i < 10;i++){
    nums[i] = new string [20];
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
    
    for (char y = 'a'; y <= 'z'; y++) {
        writech(fid,y);
        int i = y;
        string x = ((string)i);
        writech(fid,':');
        for(int i = 0 ; i < strlen(x);i++)
            writech(fid,x[i]);
        if( y != 'z'){
            writech(fid,'\r');
            writech(fid,'\n');
        }
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

def bigreal sqrt(bigreal n){
    bigreal a = n/2;
    for(int i = 0;i < 40;i++){
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