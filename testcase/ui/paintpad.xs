native<extension.predefined>{
    struct EventCallback{
        def virtual bool callback(int id);
    };

    struct MouseEventCallback{
        def virtual bool callback(int x,int y);
    };

    //void setCallback(EventCallback ec);
}

native<extension.ui>{
    "openPad":int openPadWithName(int w,int h,string name);
    int drawLine(int x1,int y1,int x2,int y2);
    int drawPoint(int x,int y);
    int addLine(int x1,int y1,int x2,int y2);
    int addPoint(int x,int y);
	"AddString":int addString(string s,int x,int y);
    int setLine(int id,int x1,int y1,int x2,int y2);
    int setPoint(int id,int x,int y);
    int setString(int id,string s,int x,int y);
    int setBrushColor(int r,int g,int b);
    int paint();
    int closePad();
    int clearPad();
}


native<extension.ui>{
    "EventLoop":bool loopForKeyboard(EventCallback f);
    "MouseEventLoop":bool loopForMouse(MouseEventCallback f);
}

def int openPad(int w,int h){
    return openPadWithName(w,h,"Script");
}

def void addRect(int x,int y,int w,int height){
    addLine(x,y,x+w,y);
    addLine(x,y,x,y+height);
    addLine(x,y+height,x+w,y+height);
    addLine(x+w,y,x+w,y+height);
}

struct Point{
    int x;
    int y;
    def this(int x,int y);
    
    def void init(int x,int y){
        this.x = x;
        this.y = y;
    }

    @+
    def Point add(Point p){
        return new Point(this.x + p.x,this.y + p.y);
    }

    @-
    def Point sub(Point p){
        return new Point(this.x - p.x,this.y - p.y);
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

    def void init(int width,int height){
        this.width = width;
        this.height = height;
        this.center = new Point(0,0);
        openPad(width,height);
        paint();
        this.brushcolor = new Color(0,0,0);
        this.rects = new DynamicArray(10);
    }

    def virtual int addPoint(Point p){
        Point real_p = p + this.center;
        if(this.width > real_p.x && real_p.x >= 0
            && this.height > real_p.y && real_p.y >= 0){
            addPoint(real_p.x,real_p.y);
        }
    }

    def virtual int setPoint(int id,Point p){
        Point real_p = p + this.center;
        if(this.width > real_p.x && real_p.x >= 0
            && this.height > real_p.y && real_p.y >= 0){
            return setPoint(id,real_p.x,real_p.y);
        }
        return -1;
    }
    
    def virtual void addRect(Point o,int width,int height){
        addRect(o.x + this.center.x,o.y + this.center.y,width,height);
    }

    def virtual int addString(Point pos,string text){
        return addString(text,pos.x+ this.center.x,pos.y+ this.center.y);
    }

    def virtual int setString(int id,Point pos,string text){
        return setString(id,text,pos.x+ this.center.x,pos.y+ this.center.y);
    }

    def virtual void draw(){
        paint();
    }

    def void clear(){
        clearPad();
    }

    def void close(){
        closePad();
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
        setBrushColor(c.r,c.g,c.b);
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
        println("CLICKED " + this.text);
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
    Point o;
    int width,height;

    def this(Point o,int width,int height){
        this.o = o;
        this.width = width;
        this.height = height;
    }
    
    def bool contains(Point p){
        Point o = this.o;
        return p.x >= o.x && p.x <= o.x + this.width
                && p.y >= o.y && p.y <= o.y+ this.height;
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
        this.eps = new DynamicArray(20);
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

struct MouseAdapter2:MouseEventCallback{
    Graphics    g;
    Screen      scr;
    EventPool   e;
    cal_state   cs;
    
    def this(Graphics g,Screen s,EventPool e){
        this.g = g;
        this.scr = s;
        this.e = e;
        this.cs = new cal_state();
    }

    def override bool callback(int x,int y){
        println("" + x + "," + y);
        this.e.onclick(new Point(x,y),this.g,this.scr,this.cs);
        return true;
    }

} 

/*
 *    _____
 *   |_____|
 *   |_|_|_|
 *   |_|_|_|
 *   |_|_|_|
 *   |_|_|_|
 */


{
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
        ep.addListener(new Region(new Point(o.x,o.y),20,20),bs[i]);
        break;
       case 1:case 2:
        o = o + new Point(20,0);
        ep.addListener(new Region(new Point(o.x,o.y),20,20),bs[i]);
        break;
       case 3:
        o = o + new Point(20,0);
        ep.addListener(new Region(new Point(o.x,o.y),20,20),bs[i]);
        o = o + new Point(-60,0);
        break;
       }
       
    }

    g.init(82,122);
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

    g.draw();
    g.setCenter(new Point(20,10));
    loopForMouse(new MouseAdapter2(g,scr,ep));
}
