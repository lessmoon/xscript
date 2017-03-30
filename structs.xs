struct SimpleFile{
    //init function
    def this(string arg0);
    //functions
    def bool isFile();
    def bool canWrite();
    def SimpleFile getParent();
    def string getName();
    def bool isHidden();
    def bool canRead();
    def bool exists();
    def bool isDirectory();
    def bool createNewFile();
    def bool mkdir();
}
struct FileInputStream{
    //init function
    def this(SimpleFile arg0);
    //functions
    def void open(SimpleFile arg0);
    def void close();
    def int readChar();
    def int available();
    def bigint skip(bigint arg0);
}
struct FileOutputStream{
    //init function
    def this(SimpleFile arg0,bool arg1);
    //functions
    def void open(SimpleFile arg0,bool arg1);
    def void writeInt(int arg0);
    def void close();
    def void flush();
}
struct File{
    //variables
    string fname;
    SimpleFile file;
    FileInputStream fis;
    FileOutputStream fos;
    //init function
    def this();
    //functions
    def bool open(string fname,bool append);
    def void writech(char c);
    def string file_name();
    def void close();
    def bool is_open();
    def int readch();
}
struct Time{
    //variables
    int hour;
    int minute;
    int second;
}
struct MyTime : Time{
    //functions
    def string toString();
}
struct StringBuffer{
    //init function
    def this();
    //functions
    def StringBuffer appendCharacter(char arg0);
    def string toString();
    def StringBuffer insert(int arg0,string arg1);
    def StringBuffer append(string arg0);
    def StringBuffer reverse();
    def StringBuffer delete(int arg0,int arg1);
    def StringBuffer setCharAt(int arg0,char arg1);
    def void reserve(int arg0);
}
struct Timer{
    //variables
    int duration;
    int start;
    //functions
    def void clear();
    def int getDuration();
    def void resume();
    def void pause();
    def void start();
}
struct Content{
    //virtual functions
    def virtual bool toBool();
    //pure virtual functions
    def virtual string toString();
}
struct Comparable : Content{
    //functions
    def bool more(Comparable a);
    //virtual functions
    def virtual bool toBool();
    //pure virtual functions
    def virtual string toString();
    def virtual bool less(Comparable a);
}
struct BoolContent : Content{
    //variables
    bool value;
    //init function
    def this(bool value);
    //virtual functions
    def virtual string toString();
    def virtual bool toBool();
}
struct HashContent : Content{
    //virtual functions
    def virtual bool toBool();
    //pure virtual functions
    def virtual string toString();
    def virtual int hash();
    def virtual bool equals(HashContent c);
}
struct IntContent : Comparable{
    //variables
    int val;
    //init function
    def this(int val);
    //functions
    def int toInt();
    //virtual functions
    def virtual string toString();
    def virtual bool less(Comparable a);
    def virtual bool toBool();
}
struct StringContent : Content{
    //variables
    string val;
    //init function
    def this(string val);
    //virtual functions
    def virtual string toString();
    def virtual bool toBool();
}
struct StringHashContent : HashContent{
    //variables
    string value;
    int hash;
    bool hashed;
    //init function
    def this(string value);
    //virtual functions
    def virtual string toString();
    def virtual bool toBool();
    def virtual int hash();
    def virtual bool equals(HashContent c);
}
struct Iterator{
    //virtual functions
    def virtual bool hasNext();
    def virtual void next();
    //pure virtual functions
    def virtual Content getValue();
}
struct Runnable{
    //pure virtual functions
    def virtual void run();
}
struct Thread{
    //init function
    def this(Runnable arg0);
    //functions
    def bigint getThreadId();
    def string getName();
    def void join(int arg0);
    def void setName(string arg0);
    def bool equals(Thread arg0);
    def bool interrupt();
    def bool start();
}
struct MutexLock{
    //init function
    def this();
    //functions
    def bool tryLock();
    def bool release();
    def bool wait();
}
struct MyTrigger{
    //init function
    def this();
    //functions
    def void triggerAll();
    def bool wait();
}
struct Trigger{
    //variables
    MyTrigger imp;
    //init function
    def this();
    //functions
    def void triggerAll();
    def void wait();
}
struct Condition{
    //pure virtual functions
    def virtual bool isTrue();
}
struct Schedule : Runnable{
    //variables
    int interval;
    Condition cond;
    Runnable run;
    //init function
    def this(int interval,Condition cond,Runnable run);
    //virtual functions
    def virtual void run();
}
struct RepeatSchedule : Condition{
    //virtual functions
    def virtual bool isTrue();
}
struct Timer2{
    //variables
    Thread thread;
    //init function
    def this(Runnable run,int interval);
    //functions
    def void stop();
    def void start();
}
struct Timer2Adapter : Runnable{
    //variables
    Timer2 timer;
    //init function
    def this(int interval);
    //functions
    def void stop();
    def void start();
    //virtual functions
    def virtual void run();
}
struct AtomicInteger{
    //variables
    int value;
    MutexLock lock;
    Trigger n;
    //init function
    def this();
    //functions
    def int getAndDecrement();
    def string toString();
    def int get();
    def void waitAndDecrement(int min);
    def int getAndSet(int value);
    def int setAndGet(int value);
    def void waitAndIncrement(int max);
    def int getAndIncrement();
    def int decrementAndGet();
    def void set(int value);
    def int incrementAndGet();
}
struct Lock{
    //pure virtual functions
    def virtual void lock();
    def virtual void unlock();
}
struct ReadLock : Lock{
    //variables
    Lock writeLock;
    //init function
    def this(Lock write);
    //virtual functions
    def virtual void lock();
    def virtual void unlock();
}
struct AsyncRunnable : Runnable{
    //variables
    Content value;
    //virtual functions
    def virtual void run();
    //pure virtual functions
    def virtual Content get();
}
struct Future{
    //variables
    Thread t;
    AsyncRunnable r;
    //init function
    def this(AsyncRunnable r);
    //functions
    def Content get();
}
struct Stream{
    //functions
    def Stream map(Consumer c);
    def int count();
    def void forEach(Consumer c);
    def Stream sort(Comparator c);
    def Stream filter(Consumer c);
    def Sequence reduce(Collector c);
    //pure virtual functions
    def virtual Iterator next();
}
struct Consumer{
    //pure virtual functions
    def virtual Content apply(Content c);
}
struct Sequence{
    //virtual functions
    def virtual void forEach(Consumer c);
    def virtual Stream stream();
    //pure virtual functions
    def virtual void add(Content c);
    def virtual bool isEmpty();
    def virtual int size();
    def virtual Iterator iterator();
}
struct Collector{
    //variables
    Sequence seq;
    //init function
    def this(Sequence s);
    //virtual functions
    def virtual void feed(Content c);
    def virtual Sequence collect();
}
struct Comparator{
    //pure virtual functions
    def virtual int compare(Content a,Content b);
}
struct TransformStream : Stream{
    //variables
    Stream of;
    //init function
    def this(Stream of);
    //pure virtual functions
    def virtual Iterator next();
}
struct FilterStream : TransformStream{
    //variables
    Consumer filter;
    //init function
    def this(Stream of,Consumer filter);
    //virtual functions
    def virtual Iterator next();
}
struct MapStream : TransformStream{
    //variables
    Consumer mapper;
    //init function
    def this(Stream of,Consumer mapper);
    //virtual functions
    def virtual Iterator next();
}
struct AISofIterator#06668 : Iterator{
    //variables
    Content value;
    //init function
    def this(Content value);
    //virtual functions
    def virtual bool hasNext();
    def virtual Content getValue();
    def virtual void next();
}
struct SequenceStream : Stream{
    //variables
    Iterator iterator;
    //init function
    def this(Sequence seq);
    //virtual functions
    def virtual Iterator next();
}
struct binode{
    //variables
    binode next;
    binode prev;
    Content value;
    //init function
    def this(Content value,binode prev,binode next);
    //functions
    def string toString();
}
struct List : Sequence{
    //variables
    binode head;
    binode tail;
    //init function
    def this();
    //functions
    def Content pop_front();
    def string toString();
    def binode front();
    def void push_back(Content value);
    def binode back();
    def Content pop_back();
    def void push_front(Content value);
    //virtual functions
    def virtual void add(Content value);
    def virtual bool isEmpty();
    def virtual void forEach(Consumer c);
    def virtual Stream stream();
    def virtual int size();
    def virtual Iterator iterator();
}
struct AISofIterator#16139 : Iterator{
    //variables
    binode node;
    //init function
    def this(binode inode);
    //virtual functions
    def virtual bool hasNext();
    def virtual Content getValue();
    def virtual void next();
}
struct AISofCollector#211327 : Collector{
    //init function
    def this();
    //virtual functions
    def virtual void feed(Content c);
    def virtual Sequence collect();
}
struct ConcurrentQueue{
    //variables
    AtomicInteger full;
    AtomicInteger empty;
    MutexLock lock;
    List list;
    //init function
    def this();
    //functions
    def void put(Content i);
    def int size();
    def Content pop();
}
struct point{
    //variables
    int x;
    int y;
}
struct Ratio{
    //variables
    int num;
    int den;
    //functions
    def string toString();
    def Ratio add(Ratio r);
    def Ratio sub(Ratio r);
    def Ratio div(Ratio r);
    def Ratio reduce();
    def Ratio mult(Ratio r);
    def void init(int num,int den);
    def bool equals(Ratio r);
}
struct Expr{
    //pure virtual functions
    def virtual real getValue();
}
struct Arith : Expr{
    //variables
    Expr e1;
    Expr e2;
    //functions
    def void init(Expr e1,Expr e2);
    //pure virtual functions
    def virtual real getValue();
}
struct Add : Arith{
    //virtual functions
    def virtual real getValue();
}
struct Sub : Arith{
    //virtual functions
    def virtual real getValue();
}
struct Mult : Arith{
    //virtual functions
    def virtual real getValue();
}
struct Div : Arith{
    //virtual functions
    def virtual real getValue();
}
struct Const : Expr{
    //variables
    real value;
    //functions
    def void setValue(int v);
    //virtual functions
    def virtual real getValue();
}
struct Var : Const{
    //virtual functions
    def virtual real getValue();
}
struct Token{
    //variables
    int tag;
    //functions
    def void setTag(int d);
    //virtual functions
    def virtual string toString();
}
struct Num : Token{
    //variables
    real value;
    //functions
    def void init(real value);
    //virtual functions
    def virtual string toString();
}
struct lexer{
    //variables
    int no;
    char peek;
    string poly;
    //functions
    def Token scan();
    def void readch();
    def void init(string poly);
}
struct parser{
    //variables
    lexer lex;
    Token look;
    Var xVar;
    //functions
    def Expr add();
    def Expr expr();
    def Expr term();
    def Expr mult();
    def void init(lexer l);
    def Var getVar();
    def void next();
}
struct DynamicArray : Sequence{
    //variables
    int capcity;
    int size;
    Content[] content;
    //init function
    def this(int size);
    //functions
    def string toString();
    def Content get(int i);
    def void clear();
    def void reset_capcity(int c);
    def int capcity();
    def Content first();
    def Content last();
    def void push_back(Content c);
    def Content set(int i,Content c);
    def bool empty();
    def void pop_back();
    //virtual functions
    def virtual void add(Content c);
    def virtual bool isEmpty();
    def virtual void forEach(Consumer c);
    def virtual Stream stream();
    def virtual int size();
    def virtual Iterator iterator();
}
struct AISofIterator#32934 : Iterator{
    //variables
    int idx;
    DynamicArray darray;
    //init function
    def this(DynamicArray darray);
    //virtual functions
    def virtual bool hasNext();
    def virtual Content getValue();
    def virtual void next();
}
struct AISofCollector#413127 : Collector{
    //init function
    def this();
    //virtual functions
    def virtual void feed(Content c);
    def virtual Sequence collect();
}
struct HashPair{
    //variables
    HashContent key;
    Content value;
    //init function
    def this(HashContent key,Content value);
    //functions
    def string toString();
}
struct HashMapNode{
    //variables
    HashPair value;
    HashMapNode next;
    //init function
    def this(HashPair value,HashMapNode next);
    //functions
    def string toString();
}
struct HashMap{
    //variables
    int size;
    int capcity;
    HashMapNode[] map;
    //init function
    def this();
    //functions
    def string toString();
    def void clear();
    def HashPair get(HashContent key);
    def void rehash(int newcapcity);
    def HashPair remove(HashContent key);
    def HashPair set(HashContent key,Content val);
    def int size();
}
struct PriorityQueue : Sequence{
    //variables
    int size;
    int capacity;
    Comparable[] contents;
    //init function
    def this();
    //functions
    def string toString();
    def void clear();
    def Comparable top();
    def Comparable pop();
    def void swap(int i,int j);
    def void push(Comparable e);
    //virtual functions
    def virtual void add(Content c);
    def virtual bool isEmpty();
    def virtual void forEach(Consumer c);
    def virtual Stream stream();
    def virtual int size();
    def virtual Iterator iterator();
}
struct AISofIterator#512131 : Iterator{
    //variables
    PriorityQueue p;
    Content value;
    //init function
    def this(PriorityQueue q);
    //virtual functions
    def virtual bool hasNext();
    def virtual Content getValue();
    def virtual void next();
}
struct ComparableProxy : Comparable{
    //variables
    Comparator compare;
    Content value;
    //init function
    def this(Content value,Comparator compare);
    //virtual functions
    def virtual string toString();
    def virtual bool less(Comparable b);
    def virtual bool toBool();
}
struct AISofCollector#619627 : Collector{
    //init function
    def this();
    //virtual functions
    def virtual void feed(Content c);
    def virtual Sequence collect();
}
struct Font{
    //init function
    def this(string arg0,int arg1,int arg2);
    //functions
    def string getFontName();
}
struct PaintPadX{
    //init function
    def this(string name,int width,int height);
    //functions
    def void open();
    def Font getFont();
    def void close();
    def void setBrushColor(int arg0,int arg1,int arg2);
    def bool setStringColor(int arg0);
    def bool setStringPosition(int arg0,int arg1,int arg2);
    def bool setCircleColor(int arg0);
    def int addLine(int arg0,int arg1,int arg2,int arg3);
    def bool setPoint(int arg0,int arg1,int arg2);
    def void redraw();
    def bool setCircleRadius(int arg0,int arg1);
    def void clearPointAndLine();
    def bool setCircle(int arg0,int arg1,int arg2);
    def bool setPointColor(int arg0);
    def int addCircle(int arg0,int arg1,int arg2);
    def void clear();
    def bool setLineColor(int arg0);
    def bool setString(int arg0,string arg1);
    def void clearString();
    def int addString(string arg0,int arg1,int arg2);
    def bool setLine(int arg0,int arg1,int arg2,int arg3,int arg4);
    def int addPoint(int arg0,int arg1);
    def void setFont(Font arg0);
    //virtual functions
    def virtual void onPress(int arg0);
    def virtual void onClose();
    def virtual void onMouseClick(int arg0,int arg1,int arg2);
    def virtual void onClick(int arg0);
}
struct PaintPad : PaintPadX{
    //variables
    Trigger t;
    //init function
    def this(string name,int width,int height);
    //functions
    def void show();
    def void wait();
    //virtual functions
    def virtual void onPress(int arg0);
    def virtual void onClose();
    def virtual void onMouseClick(int arg0,int arg1,int arg2);
    def virtual void onClick(int arg0);
}
struct Point{
    //variables
    int x;
    int y;
    //init function
    def this(int x,int y);
    //functions
    def Point add(Point p);
    def Point sub(Point p);
    def void init(int x,int y);
}
struct Color{
    //variables
    int r;
    int g;
    int b;
    //init function
    def this(int r,int g,int b);
}
struct Graphics{
    //variables
    int width;
    int height;
    Point center;
    Color brushcolor;
    DynamicArray rects;
    PaintPad pad;
    //functions
    def void clear();
    def void setCenter(Point center);
    def void transite(Point offset);
    def void close();
    def void setBrushColor(Color c);
    def Point getCenter();
    def void wait();
    def void init(PaintPad pad,int width,int height);
    def Color getBrushColor();
    //virtual functions
    def virtual void addRect(Point o,int width,int height);
    def virtual void draw();
    def virtual int setString(int id,Point pos,string text);
    def virtual void show();
    def virtual int addString(Point pos,string text);
    def virtual int addPoint(Point p);
    def virtual int setPoint(int id,Point p);
}
struct Screen{
    //variables
    string text;
    int index;
    //init function
    def this();
    //functions
    def void add(Graphics g);
    def void clear(Graphics g);
    def string getContent();
    def void setText(Graphics g,string text);
    def void appendText(Graphics g,string text);
}
struct cal_state{
    //variables
    bool is_result;
    char operand;
    real prev;
    bool has_dot;
    real present;
    real pre_scal;
    //init function
    def this();
    //functions
    def void reset();
}
struct Button{
    //variables
    string text;
    Color color;
    int id;
    int index;
    //init function
    def this(string text,int id,Color c);
    //functions
    def void onclick(Graphics g,Screen scr,cal_state cs);
    def void add(Graphics g);
    def void setString(Graphics g,string text);
}
struct Region{
    //variables
    Point o;
    int width;
    int height;
    //init function
    def this(Point o,int width,int height);
    //functions
    def bool contains(Point p);
}
struct PairContent : Content{
    //variables
    Button b;
    Region r;
    //init function
    def this(Button b,Region r);
    //virtual functions
    def virtual string toString();
    def virtual bool toBool();
}
struct EventPool{
    //variables
    DynamicArray eps;
    //init function
    def this();
    //functions
    def void onclick(Point p,Graphics g,Screen scr,cal_state cs);
    def void addListener(Region r,Button b);
}
struct Calculator : PaintPad{
    //variables
    EventPool pool;
    Graphics g;
    Screen scr;
    cal_state cs;
    //init function
    def this(string title,int width,int height,EventPool pool,Graphics g,Screen scr);
    //virtual functions
    def virtual void onPress(int arg0);
    def virtual void onClose();
    def virtual void onMouseClick(int bid,int x,int y);
    def virtual void onClick(int arg0);
}
struct CyclePaintPad : Timer2Adapter{
    //variables
    PaintPad pad;
    //init function
    def this(PaintPad pad,int interval);
    //virtual functions
    def virtual void run();
}
struct Function : Content{
    //functions
    def void procedure(Runtime r,string[] args);
    def void preprocessArgs(Runtime r,string[] args);
    //virtual functions
    def virtual string toString();
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
    //pure virtual functions
    def virtual void run(Runtime r,string[] args);
}
struct RuntimeBasic{
    //variables
    HashMap varMap;
    HashMap labelMap;
    HashMap functionMap;
    int index;
    //init function
    def this();
    //functions
    def Function getFunction(string funcname);
    def void jump(string label);
    def void sleep(int second);
    def void registerFunction(string funcname,Function function);
    def void setVar(string varname,string val);
    def string getVar(string varname);
    //pure virtual functions
    def virtual void open(string filename);
    def virtual void step();
}
struct Runtime : RuntimeBasic{
    //variables
    DynamicArray instructions;
    //init function
    def this();
    //functions
    def void addLable(string label);
    def bool isEnd();
    def void addInstructions2(string function,string[] args);
    def void addInstructions(Instruction i);
    def void clearInstructions();
    //virtual functions
    def virtual void step();
    //pure virtual functions
    def virtual void open(string filename);
}
struct Instruction : Content{
    //variables
    string[] args;
    string function;
    //init function
    def this(string function,string[] args);
    //functions
    def void run(Runtime r);
    //virtual functions
    def virtual string toString();
    def virtual bool toBool();
}
struct RPGToken : Content{
    //variables
    int tag;
    //virtual functions
    def virtual string toString();
    def virtual bool toBool();
}
struct ValueToken : RPGToken{
    //variables
    string value;
    //init function
    def this(string str);
    //virtual functions
    def virtual string toString();
    def virtual bool toBool();
}
struct SymbolToken : RPGToken{
    //init function
    def this(int c);
    //virtual functions
    def virtual string toString();
    def virtual bool toBool();
}
struct RPGLexer{
    //variables
    File f;
    int peak;
    int lineno;
    int index;
    //init function
    def this();
    //functions
    def void open(string filename);
    def void close();
    def RPGToken scan();
    def void readch();
    def bool check(int c);
}
struct RPGParser{
    //variables
    RPGToken look;
    RPGLexer lexer;
    //init function
    def this();
    //functions
    def void match(int id);
    def void move();
    def void parse(string file,Runtime r);
    def string value();
    def string[] args();
    def void error(string msg);
    def Instruction instruction();
    def bool check(int id);
    def void statement(Runtime r);
}
struct RPGRuntime : Runtime{
    //variables
    RPGParser parser;
    //init function
    def this(RPGParser p);
    //functions
    def void run();
    //virtual functions
    def virtual void open(string filename);
    def virtual void step();
}
struct Sleep : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct Choice : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct Set : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct Cond : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct Select : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct Print : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct StopPrint : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct Jump : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct TypeString : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct RPGTime : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct RPGAdd : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct RPGCase : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct Open : Function{
    //virtual functions
    def virtual string toString();
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
    def virtual string preprocessArg(Runtime r,string arg);
}
struct TestF{
    //variables
    int a;
    //pure virtual functions
    def virtual void func();
}
struct AISofTestF#93131 : TestF{
    //variables
    string @b;
    int @cc;
    //init function
    def this();
    //virtual functions
    def virtual void func();
}
struct AISofTestF#82722 : TestF{
    //variables
    string @b;
    //init function
    def this();
    //virtual functions
    def virtual void func();
}
struct AISofTestF#72423 : TestF{
    //variables
    int @a;
    string @b;
    //init function
    def this();
    //virtual functions
    def virtual void func();
}
struct ScrollTextOutput{
    //variables
    PaintPad x;
    int width;
    int height;
    int[] ids;
    List contents;
    int line;
    //init function
    def this(int width_tile,int height_tile);
    //functions
    def void open();
    def void close();
    def void addString(string str,int r,int p,int g);
    def void update();
    def void wait();
    def void changeLine();
    def void addCharacter(char c,int r,int p,int g);
}
struct base{
    //pure virtual functions
    def virtual base getBase();
}
struct derive : base{
    //functions
    def void test();
    //pure virtual functions
    def virtual base getBase();
}
struct rrr{
    //functions
    def void test(derive d);
}
struct MyPaintPad : PaintPad{
    //init function
    def this();
    //virtual functions
    def virtual void onPress(int bid);
    def virtual void onClose();
    def virtual void onMouseClick(int arg0,int arg1,int arg2);
    def virtual void onClick(int bid);
}
struct Game : CyclePaintPad{
    //variables
    int cid;
    real x;
    real y;
    real v_x;
    real v_y;
    int lid;
    //init function
    def this();
    //functions
    def void open();
    def void wait();
    //virtual functions
    def virtual void run();
}
struct baseA{
    //variables
    int id;
    //functions
    def void init(int id);
    //virtual functions
    def virtual string who();
}
struct baseB : baseA{
    //init function
    def this(int i);
    //virtual functions
    def virtual string who();
}
struct deriveC : baseB{
    //init function
    def this(int i);
    //virtual functions
    def virtual string who();
}
struct MyXPaintPad : PaintPad{
    //variables
    int lastx;
    int lasty;
    //init function
    def this(string name,int width,int height);
    //virtual functions
    def virtual void onPress(int arg0);
    def virtual void onClose();
    def virtual void onMouseClick(int code,int x,int y);
    def virtual void onClick(int code);
}
struct PrintCount : Runnable{
    //variables
    int i;
    //init function
    def this();
    //virtual functions
    def virtual void run();
}
struct PairContent2 : Content{
    //variables
    int id;
    int value;
    //init function
    def this(int id,int value);
    //virtual functions
    def virtual string toString();
    def virtual bool toBool();
}
struct PrintNumber : Runnable{
    //variables
    int id;
    //init function
    def this(int id);
    //virtual functions
    def virtual void run();
}
struct AISofConsumer#1041127 : Consumer{
    //virtual functions
    def virtual Content apply(Content i);
}
struct AISofConsumer#1141835 : Consumer{
    //virtual functions
    def virtual Content apply(Content i);
}
struct AISofConsumer#1242224 : Consumer{
    //virtual functions
    def virtual Content apply(Content i);
}
struct AISofComparator#1342627 : Comparator{
    //virtual functions
    def virtual int compare(Content a,Content b);
}
struct AISofConsumer#1443028 : Consumer{
    //virtual functions
    def virtual Content apply(Content i);
}
struct JustOnce : Runnable{
    //virtual functions
    def virtual void run();
}
struct AISofRunnable#1552039 : Runnable{
    //variables
    PaintPad @pad;
    //init function
    def this();
    //virtual functions
    def virtual void run();
}
struct shape{
    //variables
    string name;
    //functions
    def string toString();
    def void init(string name);
    //pure virtual functions
    def virtual void draw();
}
struct square : shape{
    //variables
    int width;
    //functions
    def void setWidth(int w);
    //virtual functions
    def virtual void draw();
}
struct rectangle : square{
    //variables
    int length;
    //functions
    def void setLength(int l);
    //virtual functions
    def virtual void draw();
}
struct ChessPad : PaintPad{
    //init function
    def this(string name,int w,int h);
    //virtual functions
    def virtual void onPress(int arg0);
    def virtual void onClose();
    def virtual void onMouseClick(int arg0,int arg1,int arg2);
    def virtual void onClick(int id);
}
struct AISofRunnable#1678039 : Runnable{
    //variables
    bool[][] @world;
    bool[][] @world1;
    bool[][] @world2;
    PaintPad @pad;
    //init function
    def this();
    //virtual functions
    def virtual void run();
}
struct complex{
    //variables
    real r;
    real img;
    //functions
    def string toString();
    def complex add(complex x);
    def bool isGreaterThan(complex c);
}
struct AISofRunnable#1798239 : Runnable{
    //variables
    PaintPad @pad;
    //init function
    def this();
    //virtual functions
    def virtual void run();
}
struct CORD{
    //variables
    real x;
    real y;
    //functions
    def string toString();
    def void init(real x,real y);
}
struct tree_node{
    //variables
    tree_node left;
    tree_node right;
    int value;
}
