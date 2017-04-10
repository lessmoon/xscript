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
struct Object{
    //init function
    def this();
    //functions
    def StructType getType();
}
struct StructType{
    //init function
    def this();
    //functions
    def string getName();
    def bool isChildOf(StructType arg0);
    def bool equals(StructType arg0);
    def Object newInstance(Object[] arg0);
}
struct JNClass{
    //init function
    def this(string arg0);
    //functions
    def bool isAssignableFrom(JNClass arg0);
    def bool isInstance(JNObject arg0);
    def string getName();
    def JNMethod getMethod(string arg0,JNClass[] arg1);
    def bool isNull();
    def bool equals(JNClass arg0);
    def JNObject newInstance(JNObject[] arg0);
}
struct JNMethod{
    //init function
    def this();
    //functions
    def string getName();
    def JNClass getReturnType();
    def JNObject invoke(JNObject arg0,JNObject[] arg1);
}
struct JNObject{
    //init function
    def this();
    //functions
    def JNObject newBoolean(bool arg0);
    def string toString();
    def JNObject newString(string arg0);
    def bigreal castDouble();
    def JNObject newCharacter(char arg0);
    def string castString();
    def bigreal castBigReal();
    def JNClass getClass();
    def JNObject Null();
    def JNObject newFloat(real arg0);
    def JNObject newLong(int arg0);
    def int castInt();
    def JNObject newBigInteger(bigint arg0);
    def char castChar();
    def bigint castBigInt();
    def JNObject newInteger(int arg0);
    def bool isNull();
    def real castReal();
    def bool castBool();
    def bigint castLong();
    def JNObject newBigDecimal(bigreal arg0);
    def JNObject newDouble(real arg0);
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
    def string toString();
    def StringBuffer append(string arg0);
    def StringBuffer reverse();
    def void reserve(int arg0);
    def StringBuffer setCharAt(int arg0,char arg1);
    def StringBuffer delete(int arg0,int arg1);
    def StringBuffer appendCharacter(char arg0);
    def StringBuffer insert(int arg0,string arg1);
}
struct Timer{
    //variables
    int duration;
    int start;
    //functions
    def void start();
    def int getDuration();
    def void clear();
    def void pause();
    def void resume();
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
    def virtual bool equals(HashContent c);
    def virtual int hash();
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
    def virtual bool equals(HashContent c);
    def virtual bool toBool();
    def virtual int hash();
}
struct Consumer{
    //pure virtual functions
    def  default virtual Content apply(Content c);
}
struct Stream{
    //functions
    def int count();
    def Stream reverse();
    def Sequence reduce(Collector c);
    def Stream map(Consumer c);
    def Stream skip(int c);
    def void forEach(Consumer c);
    def Stream filter(Consumer c);
    def Stream sort(Comparator c);
    //pure virtual functions
    def  default virtual Iterator next();
}
struct Iterator{
    //functions
    def void forEachRemained(Consumer action);
    def Stream stream();
    //virtual functions
    def virtual void next();
    def virtual bool hasNext();
    //pure virtual functions
    def  default virtual Content getValue();
}
struct Runnable{
    //pure virtual functions
    def  default virtual void run();
}
struct Thread{
    //init function
    def this(Runnable arg0);
    //functions
    def bigint getThreadId();
    def bool start();
    def string getName();
    def void setName(string arg0);
    def void join(int arg0);
    def bool interrupt();
    def bool equals(Thread arg0);
}
struct MutexLock{
    //init function
    def this();
    //functions
    def bool wait();
    def bool tryLock();
    def bool release();
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
    def default virtual void run();
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
    def void start();
    def void stop();
}
struct Timer2Adapter : Runnable{
    //variables
    Timer2 timer;
    //init function
    def this(int interval);
    //functions
    def void start();
    def void stop();
    //virtual functions
    def default virtual void run();
}
struct AtomicInteger{
    //variables
    int value;
    MutexLock lock;
    Trigger n;
    //init function
    def this();
    //functions
    def string toString();
    def void waitAndDecrement(int min);
    def int get();
    def int incrementAndGet();
    def int setAndGet(int value);
    def int getAndSet(int value);
    def int getAndIncrement();
    def int getAndDecrement();
    def void waitAndIncrement(int max);
    def int decrementAndGet();
    def void set(int value);
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
    def default virtual void run();
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
struct Sequence{
    //virtual functions
    def virtual Stream stream();
    def virtual void forEach(Consumer c);
    //pure virtual functions
    def virtual int size();
    def virtual Iterator iterator();
    def virtual void add(Content c);
    def virtual bool isEmpty();
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
    def  default virtual int compare(Content a,Content b);
}
struct TransformStream : Stream{
    //variables
    Stream of;
    //init function
    def this(Stream of);
    //pure virtual functions
    def  default virtual Iterator next();
}
struct FilterStream : TransformStream{
    //variables
    Consumer filter;
    //init function
    def this(Stream of,Consumer filter);
    //virtual functions
    def default virtual Iterator next();
}
struct MapStream : TransformStream{
    //variables
    Consumer mapper;
    //init function
    def this(Stream of,Consumer mapper);
    //virtual functions
    def default virtual Iterator next();
}
struct lambda$Iterator#07634 : Iterator{
    //variables
    MapStream @this;
    Iterator @iter;
    //init function
    def this();
    //virtual functions
    def virtual void next();
    def default virtual Content getValue();
    def virtual bool hasNext();
}
struct SequenceStream : Stream{
    //variables
    Iterator iterator;
    //init function
    def this(Sequence seq);
    //virtual functions
    def default virtual Iterator next();
}
struct lambda$Stream#114224 : Stream{
    //variables
    Iterator @this;
    //init function
    def this();
    //virtual functions
    def default virtual Iterator next();
}
struct RangeStream : Stream{
    //variables
    int i;
    int end;
    //init function
    def this(int beg,int end);
    //virtual functions
    def default virtual Iterator next();
}
struct lambda$Iterator#216334 : Iterator{
    //variables
    RangeStream @this;
    //init function
    def this();
    //virtual functions
    def virtual void next();
    def default virtual Content getValue();
    def virtual bool hasNext();
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
    def string toString();
    def Content pop_front();
    def Content pop_back();
    def binode back();
    def void push_front(Content value);
    def binode front();
    def void push_back(Content value);
    //virtual functions
    def virtual int size();
    def virtual Iterator iterator();
    def virtual Stream stream();
    def virtual void add(Content value);
    def virtual void forEach(Consumer c);
    def virtual bool isEmpty();
}
struct nested$Iterator#36139 : Iterator{
    //variables
    binode node;
    //init function
    def this(binode inode);
    //virtual functions
    def virtual void next();
    def default virtual Content getValue();
    def virtual bool hasNext();
}
struct nested$Collector#412227 : Collector{
    //init function
    def this();
    //virtual functions
    def virtual Sequence collect();
    def virtual void feed(Content c);
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
    def int size();
    def void put(Content i);
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
    def Ratio div(Ratio r);
    def void init(int num,int den);
    def Ratio mult(Ratio r);
    def bool equals(Ratio r);
    def Ratio reduce();
    def Ratio add(Ratio r);
    def Ratio sub(Ratio r);
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
    def void init(string poly);
    def void readch();
}
struct parser{
    //variables
    lexer lex;
    Token look;
    Var xVar;
    //functions
    def void init(lexer l);
    def Expr mult();
    def void next();
    def Var getVar();
    def Expr term();
    def Expr add();
    def Expr expr();
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
    def Content first();
    def void pop_back();
    def void reset_capcity(int c);
    def bool empty();
    def int capcity();
    def void clear();
    def void push_back(Content c);
    def Content last();
    def Content set(int i,Content c);
    //virtual functions
    def virtual int size();
    def virtual Iterator iterator();
    def virtual Stream stream();
    def virtual void add(Content c);
    def virtual void forEach(Consumer c);
    def virtual bool isEmpty();
}
struct nested$Iterator#52934 : Iterator{
    //variables
    int idx;
    DynamicArray darray;
    //init function
    def this(DynamicArray darray);
    //virtual functions
    def virtual void next();
    def default virtual Content getValue();
    def virtual bool hasNext();
}
struct nested$Collector#613127 : Collector{
    //init function
    def this();
    //virtual functions
    def virtual Sequence collect();
    def virtual void feed(Content c);
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
    def int size();
    def string toString();
    def HashPair get(HashContent key);
    def void rehash(int newcapcity);
    def void clear();
    def HashPair set(HashContent key,Content val);
    def HashPair remove(HashContent key);
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
    def void swap(int i,int j);
    def void push(Comparable e);
    def Comparable top();
    def Comparable pop();
    //virtual functions
    def virtual int size();
    def virtual Iterator iterator();
    def virtual Stream stream();
    def virtual void add(Content c);
    def virtual void forEach(Consumer c);
    def virtual bool isEmpty();
}
struct nested$Iterator#712131 : Iterator{
    //variables
    PriorityQueue p;
    Content value;
    //init function
    def this(PriorityQueue q);
    //virtual functions
    def virtual void next();
    def default virtual Content getValue();
    def virtual bool hasNext();
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
struct nested$Collector#819627 : Collector{
    //init function
    def this();
    //virtual functions
    def virtual Sequence collect();
    def virtual void feed(Content c);
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
    def int addString(string arg0,int arg1,int arg2);
    def void redraw();
    def void clearPointAndLine();
    def bool setLineColor(int arg0);
    def bool setStringPosition(int arg0,int arg1,int arg2);
    def bool setPointColor(int arg0);
    def void close();
    def bool setCircleRadius(int arg0,int arg1);
    def Font getFont();
    def void clear();
    def int addLine(int arg0,int arg1,int arg2,int arg3);
    def bool setCircleColor(int arg0);
    def int addCircle(int arg0,int arg1,int arg2);
    def void setBrushColor(int arg0,int arg1,int arg2);
    def void setFont(Font arg0);
    def bool setLine(int arg0,int arg1,int arg2,int arg3,int arg4);
    def void clearString();
    def bool setPoint(int arg0,int arg1,int arg2);
    def bool setCircle(int arg0,int arg1,int arg2);
    def bool setStringColor(int arg0);
    def int addPoint(int arg0,int arg1);
    def bool setString(int arg0,string arg1);
    //virtual functions
    def virtual void onPress(int arg0);
    def virtual void onClick(int arg0);
    def virtual void onMouseClick(int arg0,int arg1,int arg2);
    def virtual void onClose();
}
struct PaintPad : PaintPadX{
    //variables
    Trigger t;
    //init function
    def this(string name,int width,int height);
    //functions
    def void wait();
    def void show();
    //virtual functions
    def virtual void onPress(int arg0);
    def virtual void onClick(int arg0);
    def virtual void onMouseClick(int arg0,int arg1,int arg2);
    def virtual void onClose();
}
struct Point{
    //variables
    int x;
    int y;
    //init function
    def this(int x,int y);
    //functions
    def void init(int x,int y);
    def Point add(Point p);
    def Point sub(Point p);
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
    def Point getCenter();
    def void init(PaintPad pad,int width,int height);
    def void wait();
    def void close();
    def void clear();
    def Color getBrushColor();
    def void setBrushColor(Color c);
    def void setCenter(Point center);
    def void transite(Point offset);
    //virtual functions
    def virtual void addRect(Point o,int width,int height);
    def virtual int addString(Point pos,string text);
    def virtual int setPoint(int id,Point p);
    def virtual void draw();
    def virtual int addPoint(Point p);
    def virtual int setString(int id,Point pos,string text);
    def virtual void show();
}
struct Screen{
    //variables
    string text;
    int index;
    //init function
    def this();
    //functions
    def string getContent();
    def void appendText(Graphics g,string text);
    def void clear(Graphics g);
    def void add(Graphics g);
    def void setText(Graphics g,string text);
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
    def virtual void onClick(int arg0);
    def virtual void onMouseClick(int bid,int x,int y);
    def virtual void onClose();
}
struct CyclePaintPad : Timer2Adapter{
    //variables
    PaintPad pad;
    //init function
    def this(PaintPad pad,int interval);
    //virtual functions
    def default virtual void run();
}
struct Function : Content{
    //functions
    def void preprocessArgs(Runtime r,string[] args);
    def void procedure(Runtime r,string[] args);
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual bool toBool();
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
    def void jump(string label);
    def void sleep(int second);
    def Function getFunction(string funcname);
    def void setVar(string varname,string val);
    def string getVar(string varname);
    def void registerFunction(string funcname,Function function);
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
    def void addInstructions(Instruction i);
    def void addLable(string label);
    def bool isEnd();
    def void addInstructions2(string function,string[] args);
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
    def RPGToken scan();
    def bool check(int c);
    def void close();
    def void readch();
}
struct RPGParser{
    //variables
    RPGToken look;
    RPGLexer lexer;
    //init function
    def this();
    //functions
    def void statement(Runtime r);
    def bool check(int id);
    def string value();
    def void move();
    def void parse(string file,Runtime r);
    def void match(int id);
    def Instruction instruction();
    def string[] args();
    def void error(string msg);
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
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct Choice : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct Set : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct Cond : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct Select : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct Print : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct StopPrint : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct Jump : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct TypeString : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct RPGTime : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct RPGAdd : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct RPGCase : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
}
struct Open : Function{
    //virtual functions
    def virtual string toString();
    def virtual string preprocessArg(Runtime r,string arg);
    def virtual void run(Runtime r,string[] args);
    def virtual bool toBool();
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
    def void addString(string str,int r,int p,int g);
    def void open();
    def void wait();
    def void close();
    def void addCharacter(char c,int r,int p,int g);
    def void changeLine();
    def void update();
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
    def virtual void onClick(int bid);
    def virtual void onMouseClick(int arg0,int arg1,int arg2);
    def virtual void onClose();
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
    def default virtual void run();
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
    def virtual void onClick(int code);
    def virtual void onMouseClick(int code,int x,int y);
    def virtual void onClose();
}
struct PrintCount : Runnable{
    //variables
    int i;
    //init function
    def this();
    //virtual functions
    def default virtual void run();
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
    def default virtual void run();
}
struct nested$Consumer#938027 : Consumer{
    //virtual functions
    def default virtual Content apply(Content i);
}
struct lambda$Consumer#1038829 : Consumer{
    //virtual functions
    def default virtual Content apply(Content i);
}
struct lambda$Consumer#1138930 : Consumer{
    //virtual functions
    def default virtual Content apply(Content i);
}
struct lambda$Consumer#1239326 : Consumer{
    //virtual functions
    def default virtual Content apply(Content i);
}
struct lambda$Consumer#1339423 : Consumer{
    //virtual functions
    def default virtual Content apply(Content i);
}
struct lambda$Comparator#1439526 : Comparator{
    //virtual functions
    def default virtual int compare(Content a,Content b);
}
struct lambda$Consumer#1539627 : Consumer{
    //virtual functions
    def default virtual Content apply(Content i);
}
struct JustOnce : Runnable{
    //virtual functions
    def default virtual void run();
}
struct lambda$Runnable#1649734 : Runnable{
    //variables
    PaintPad @pad;
    //init function
    def this();
    //virtual functions
    def default virtual void run();
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
    def virtual void onClick(int id);
    def virtual void onMouseClick(int arg0,int arg1,int arg2);
    def virtual void onClose();
}
struct lambda$Runnable#1777134 : Runnable{
    //variables
    bool[][] @world;
    bool[][] @world1;
    bool[][] @world2;
    PaintPad @pad;
    //init function
    def this();
    //virtual functions
    def default virtual void run();
}
struct complex{
    //variables
    real r;
    real img;
    //functions
    def string toString();
    def bool isGreaterThan(complex c);
    def complex add(complex x);
}
struct lambda$Runnable#1897140 : Runnable{
    //variables
    PaintPad @pad;
    //init function
    def this();
    //virtual functions
    def default virtual void run();
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
