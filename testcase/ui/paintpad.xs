import"../lib/concurrent.xs";

native<extension.ui>{
    "SimpleFont":struct Font{
        def this(string name,int style,int size);
        def string getFontName();
    };

    "PaintPadX":struct PaintPadX{
        //init function
        def this(string name,int width,int height);
        //functions
        def void open();
        def bool setString(int id,string arg1);
        def bool setLineColor(int id);
        def bool setStringPosition(int id,int x,int y);
        def int addLine(int x1,int y1,int x2,int y2);
        def void clearPointAndLine();
        def void close();
        def void setBrushColor(int r,int g,int b);
        def void clearString();
        def bool setCircleRadius(int id,int radius);
        def bool setLine(int id,int x1,int y1,int x2,int y2);
        def bool setPointColor(int id);
        def int addString(string str,int x,int y);
        def void clear();
        def Font getFont();
        def bool setCircleColor(int id);
        def int addCircle(int x,int y,int radius);
        def int addPoint(int x,int y);
        def void redraw();
        def bool setPoint(int id,int x,int y);
        def bool setStringColor(int id);
        def void setFont(Font font);
        def bool setCircle(int id,int x,int y);
        //virtual functions
        def virtual void onMouseClick(int bid,int x,int y);
        def virtual void onPress(int kid);
        def virtual void onClose();
        def virtual void onClick(int kid);
    };
}

struct PaintPad:PaintPadX{
    Trigger t;

    def this(string name,int width,int height){
        super(name,width,height);
        this.t = new Trigger();
    }

    def void show(){
        super.open();
    }

    def override void onClose(){
        super.onClose();
        this.t.triggerAll();
    }

    def void wait(){
        this.t.wait();
    }
}

def void addRect(PaintPad pad,int x,int y,int w,int height){
    pad.addLine(x,y,x+w,y);
    pad.addLine(x,y,x,y+height);
    pad.addLine(x,y+height,x+w,y+height);
    pad.addLine(x+w,y,x+w,y+height);
}

struct Point{
    int x;
    int y;
    def this(int x,int y);
    
    def void init(int x,int y){
        this.x = x;
        this.y = y;
    }

    @string
    def string toString(){
        return "[" + this.x + "," + this.y + "]";
    }
    
    @+
    def Point add(Point p){
        return new Point(this.x + p.x,this.y + p.y);
    }

    @-
    def Point sub(Point p){
        return new Point(this.x - p.x,this.y - p.y);
    }

    def int dot(Point p){
        return p.x*this.x + p.y*this.y;
    }
}

def Point.this(int x,int y){
    this.init(x,y);
}


struct Color{
    int r,g,b;
    
    def this(int r,int g,int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }
}

import "../container/darray.xs";

struct Graphics {
    int width,height;
    Point center;
    Color brushcolor;
    DynamicArray rects;
    PaintPad pad;
    
    def void init(PaintPad pad,int width,int height){
        this.width = width;
        this.height = height;
        this.center = new Point(0,0);
        this.pad = pad;
        this.pad.show();
        this.brushcolor = new Color(0,0,0);
        this.rects = new DynamicArray(10);
    }

    def virtual int addPoint(Point p){
        Point real_p = p + this.center;
        if(this.width > real_p.x && real_p.x >= 0
            && this.height > real_p.y && real_p.y >= 0){
            this.pad.addPoint(real_p.x,real_p.y);
        }
    }

    def virtual int setPoint(int id,Point p){
        Point real_p = p + this.center;
        if(this.width > real_p.x && real_p.x >= 0
            && this.height > real_p.y && real_p.y >= 0){
            this.pad.setPoint(id,real_p.x,real_p.y);
            return id;
        }
        return -1;
    }
    
    def virtual void addRect(Point o,int width,int height){
        addRect(this.pad,o.x + this.center.x,o.y + this.center.y,width,height);
    }

    def virtual int addString(Point pos,string text){
        return this.pad.addString(text,pos.x+ this.center.x,pos.y+ this.center.y);
    }

    def virtual int setString(int id,Point pos,string text){
        bool r = this.pad.setString(id,text);
        this.pad.setStringPosition(id,pos.x+ this.center.x,pos.y+ this.center.y);
    }

    def virtual void draw(){
        this.pad.redraw();
    }

    def virtual void show(){
        this.pad.show();
    }
    
    def void clear(){
        this.pad.clear();
    }

    def void close(){
        this.pad.close();
    }

    def void wait(){
        this.pad.wait();
    }
    
    def void transite(Point offset){
        this.center = this.center + offset;
    }

    def void setCenter(Point center){
        this.center = center;
    }
    
    def Point getCenter(){
        return this.center;
    }

    def void setBrushColor(Color c){
        this.brushcolor = c;
        this.pad.setBrushColor(c.r,c.g,c.b);
    }

    def Color getBrushColor(){
        return this.brushcolor;
    }

}

struct Screen{
    string  text;
    int     index;

    def this(){
        this.text = "0";
        this.index = -1;
    }
    
    def void add(Graphics g){
        g.addRect(new Point(0,0),80,20);
        this.index = g.addString(new Point(0,15),this.text);
    }

    def string getContent(){
        return this.text;
    }

    def void clear(Graphics g){
        this.text = "0";
        g.setString(this.index,new Point(0,15),this.text);
        println("clear");
    }
    
    def void setText(Graphics g,string text){
        this.text = text;
        g.setString(this.index,new Point(0,15),this.text);
    }
    
    def void appendText(Graphics g,string text){
        this.text += text;
        println("app:" + this.text);
        println(g.setString(this.index,new Point(0,15),this.text));
    }
}

struct cal_state{
    bool    is_result;
    char    operand;
    real    prev;
    bool    has_dot;
    real    present;
    real    pre_scal;
    
    def this(){
        this.is_result = true;
        this.operand   = '+';
        this.prev      = 0.0;
        this.has_dot   = false;
        this.present   = 0.0;
        this.pre_scal  = 1.0;
    }
    
    def void reset(){
        this.is_result = true;
        this.operand   = '+';
        this.prev      = 0.0;
        this.has_dot   = false;
        this.present   = 0.0;
        this.pre_scal  = 1.0;
    }
}

struct Button{
    string text;
    Color  color;
    int    id;
    int    index;

    def this(string text,int id,Color c){
        this.text = text;
        this.color= c;
        this.id   = id;
        this.index = -1;
    }
    
    def void add(Graphics g){
        g.setBrushColor(this.color);
        g.addRect(new Point(0,0),20,20);
        this.index = g.addString(new Point(10,15),this.text);
    }

    def void setString(Graphics g,string text){
        this.text = text;
        g.setString(this.index,new Point(10,15),this.text);
    }

    def void onclick(Graphics g,Screen scr,cal_state cs){
        println("CLICKED " + this.text + ":" + this.id);
        switch(this.id){
        case '1':case '2':case '3':case '4':case '5':case '6':case '7':
        case '8':case '9':case '0':
            if(cs.is_result){
                cs.present = 0.0;
                cs.is_result = false;
            }
            if(cs.has_dot){
                cs.pre_scal /= 10;
                cs.present = cs.present + cs.pre_scal * (this.id - '0');
            } else {
                cs.present = cs.present*10 + (this.id - '0');
            }
            scr.setText(g,(string)cs.present);
            break;
        case '.':
            if(!cs.has_dot){
                cs.has_dot = true;
            }
            break;
        case '+':case '-':case '*':case '/':
            if(!cs.is_result){
                switch(cs.operand){
                case '+': 
                    cs.prev += cs.present;
                    break;
                case '-':
                    cs.prev -= cs.present;
                    break;
                case '*': 
                    cs.prev *= cs.present;
                    break;
                case '/':
                    cs.prev /= cs.present;
                    break;
                }
                cs.is_result = true;
                scr.setText(g,(string)cs.prev);
            }
            cs.operand   = this.id;
            break;
        case 'c':
            scr.clear(g);
            cs.reset();
            println("cls");
            break;
        }
    }
}

struct Region{
    def virtual bool contains(Point p);
    def virtual bool isCollisionWith(Region b){
        return false;
    }
    
    @string
    def virtual string toString(){
        return "";
    }
}

struct RoundRegion : Region{
    Point o;
    int   radius;
    
    def this(Point o,int radius){
        this.o = o;
        this.radius = radius;
    }

    def override bool contains(Point p){
        int x = p.x - this.o.x;
        int y = p.y - this.o.y;
        return x*x + y*y <= this.radius * this.radius;
    }
    
    def override bool isCollisionWith(const Region b){
        if(b instanceof RoundRegion){
            const auto c = (RoundRegion) b;
            auto o_x = c.o.x - this.o.x;
            auto o_y = c.o.y - this.o.y;
            auto o_x_2 = o_x * o_x;
            auto o_y_2 = o_y * o_y;
            const auto r  = c.radius + this.radius;
            return  o_x_2 + o_y_2 <= r * r;
        }
        return b.isCollisionWith(this);
    }
    
    @string
    def override string toString(){
        return "round" + this.o.toString() + ":" + this.radius;
    }
}

struct RectRegion : Region{
    Point o;
    int width,height;

    def this(Point o,int width,int height){
        this.o = o;
        this.width = width;
        this.height = height;
    }
    
    def override bool contains(Point p){
        Point o = this.o;
        return p.x >= o.x && p.x <= o.x + this.width
                && p.y >= o.y && p.y <= o.y+ this.height;
    }
    
    def override bool isCollisionWith(const Region b){
        if(b instanceof RectRegion){
            const auto h = (RectRegion) b;
            const auto x1 = this.o.x,x2 = h.o.x,
                       x1_ = this.o.x + this.width,x2_ = this.o.x + h.width,
                       y1 = this.o.y,y2 = h.o.y,
                       y1_ = this.o.y + this.height,y2_ = this.o.y + h.height;
            
            return !(x1 > x2_ || x2 > x1_||y1 > y2_ || y2 > y1_);
        } else if (b instanceof RoundRegion){
            const auto h    = (RoundRegion) b;
            const auto x1   = this.o.x,
                       x1_  = this.o.x + this.width,
                       y1   = this.o.y,
                       y1_  = this.o.y + this.height;
            auto p = new Point(h.o.x,h.o.y);           
            p.x = abs(2*p.x-(x1+x1_));
            p.y = abs(2*p.y-(y1+y1_));
            p = p.sub(new Point(this.width,this.height));
            if(p.x < 0){
                p.x = 0;
            }

            if(p.y < 0){
                p.y = 0;
            }

            return p.x * p.x + p.y * p.y <= 4 * h.radius * h.radius;
        } else {
            return b.isCollisionWith(this);
        }
    }
    
    @string
    def override string toString(){
        return "rectangle" + this.o.toString() + ":" + this.width + "," + this.height;
    }
}

struct PairContent : Content{
    Button b;
    Region r;

    def this(Button b,Region r){
        this.b = b;
        this.r = r;
    }
    
    def override string toString(){
        return "";
    }
}

struct EventPool{
    DynamicArray eps ;

    def this(){
        this.eps = new DynamicArray(1);
    }
    
    def void addListener(Region r,Button b){
        this.eps.push_back(new PairContent(b,r));
    }

    def void onclick(Point p,Graphics g,Screen scr,cal_state cs){
        int size = this.eps.size();
        for(int i = 0;i < size;i++){
            PairContent pc = (PairContent)this.eps.get(i);
            if(pc.r.contains(p)){
                pc.b.onclick(g,scr,cs);
                g.draw();
                return;
            }
        }
    }
}

struct Calculator:PaintPad{
    EventPool pool;
    Graphics g;
    Screen scr;
    cal_state cs;
    
    def this(string title,int width,int height
             ,EventPool pool,Graphics g,Screen scr){
        super(title,width,height);
        this.pool = pool;
        this.g = g;
        this.scr = scr;
        this.cs = new cal_state();
    }
    
    def override void onMouseClick(int bid,int x,int y){
        this.pool.onclick(new Point(x,y),this.g,this.scr,this.cs);
    }
}

import "../math/Math.xs";

if(_isMain_){
    
    const auto width =600,height = 800;
    
    const auto x = new PaintPad("region",width,height){
        int i;
        Point p;
        Region r;

        def this(string title,int width,int height){
            super(title,width,height);
            this.i = 0;
        }

        def override void onMouseClick(int bid,int x,int y){
            switch(this.i){
            case  0:
                this.p = new Point(x,y);
                this.addPoint(x,y);
                break;
            case  1:
                addRect(this,x,y,this.p.x - x, this.p.y - y);
                this.r = new RectRegion(new Point(min(x,this.p.x),min(this.p.y,y)),abs(this.p.x - x), abs(this.p.y - y));
                break;
            case  2:
                this.p = new Point(x,y);
                this.addPoint(x,y);
                break;
            case  3:
                Point r = new Point(x,y).sub(this.p);
                const int radius = sqrt(r.dot(r));
                this.addCircle(this.p.x - radius,this.p.y - radius,radius * 2);
                println("isCollisionWith:"+this.r.isCollisionWith(new RoundRegion(this.p,radius)));
                break;
            }
            this.redraw();
            this.i ++;
            this.i %= 4;
        }
    };
    x.show();
    x.wait();
}


/*
 *    _____
 *   |_____|
 *   |_|_|_|
 *   |_|_|_|
 *   |_|_|_|
 *   |_|_|_|
 */
if(true||_isMain_){
    Graphics g = new Graphics;
    EventPool ep = new EventPool();
    Screen scr = new Screen();

    Button[] bs = {
    new Button('1','1',new Color(255,0,0)),new Button('2','2',new Color(255,0,0)),
    new Button('3','3',new Color(255,0,0)),new Button('+','+',new Color(0,0,255)),
    new Button('4','4',new Color(255,0,0)),new Button('5','5',new Color(255,0,0)),
    new Button('6','6',new Color(255,0,0)),new Button('-','-',new Color(0,0,255)),
    new Button('7','7',new Color(255,0,0)),new Button('8','8',new Color(255,0,0)),
    new Button('9','9',new Color(255,0,0)),new Button('*','*',new Color(0,0,255)),
    new Button('c','c',new Color(0,0,255)),new Button('0','0',new Color(255,0,0)),
    new Button('.','.',new Color(0,0,255)),new Button('/','/',new Color(0,0,255))};

    int len = sizeof bs;
    Point o = new Point(20,10);
    for(int i = 0; i < len;i++){
       switch(i%4){
       case 0:
        o = o + new Point(0,20);
        ep.addListener(new RectRegion(new Point(o.x,o.y),20,20),bs[i]);
        break;
       case 1:case 2:
        o = o + new Point(20,0);
        ep.addListener(new RectRegion(new Point(o.x,o.y),20,20),bs[i]);
        break;
       case 3:
        o = o + new Point(20,0);
        ep.addListener(new RectRegion(new Point(o.x,o.y),20,20),bs[i]);
        o = o + new Point(-60,0);
        break;
       }
       
    }

    g.init(new Calculator("Calculator",122,82,ep,g,scr),82,122);
    g.transite(new Point(20,10));
    scr.add(g);
    
    for(int i = 0; i < len;i++){
       switch(i%4){
       case 0:
        g.transite(new Point(0,20));
        bs[i].add(g);
        break;
       case 1:case 2:
        g.transite(new Point(20,0));
        bs[i].add(g);
        break;
       case 3:
        g.transite(new Point(20,0));
        bs[i].add(g);
        g.transite(new Point(-60,0));
        break;
       }
    }
    g.setCenter(new Point(20,10));
    g.show();
    g.draw();
    g.wait();
}