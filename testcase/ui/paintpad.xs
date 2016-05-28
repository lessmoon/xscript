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
        return new Point(this.x + p.x,this.y + p.x);
    }

    @-
    def Point sub(Point p){
        return new Point(this.x - p.x,this.y - p.x);
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

struct Graphics {
    int width,height;
    Point center;
    Color brushcolor;
    
    def void init(int width,int height){
        this.width = width;
        this.height = height;
        this.center = new Point(0,0);
        openPad(width,height);
        paint();
        this.brushcolor = new Color(0,0,0);
    }

    def virtual void addPoint(Point p){
        Point real_p = p + this.center;
        if(this.width > real_p.x && real_p.x >= 0
            && this.height > real_p.y && real_p.y >= 0){
            addPoint(real_p.x,real_p.y);
        }
    }

    def virtual void addRect(Point o,int width,int height){
        addRect(o.x + this.center.x,o.y + this.center.y,width,height);
    }

    def virtual void addString(Point pos,string text){
        addString(text,pos.x,pos.y);
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

struct screen{
    string text;
    
    def this(){
        this.text = "0";
    }
    
    def void add(Graphics g){
        g.addRect(new Point(0,0),20,20);
        g.addString(new Point(0,20),this.text);
    }

    def string getContent(){
        return this.text;
    }

    def void clear(){
        this.text = "0";
    }
    
    def void setText(string text){
        this.text = text;
    }
}

struct Button{
    string text;
    Color  color;
    
    def this(string text,int id,Color c){
        this.text = text;
        this.color= c;
    }
    
    def void add(Graphics g){
        g.addRect(new Point(0,0),20,20);
        g.addString(new Point(0,20),this.text);
    }
    
    def void onclick(screen scr,char op,int v){
        
    }
}

struct region{
    Point o;
    int width,height;
}

struct event_pool{
    darray<region> x ;

    void addListen(){
        
    }
    
    void click(Point p){
        
    }
}

{
    Graphics g = new Graphics;
    g.init(20,20);
    new Button("2",'2').add(g);
    g.draw();
    getchar();
}
