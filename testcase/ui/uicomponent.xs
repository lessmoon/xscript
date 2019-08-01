struct UIGraphic{
    def virtual bool setString(int id,string arg1);
    def virtual bool setLineColor(int id);
    def virtual bool setStringPosition(int id,int x,int y);
    def virtual int  addLine(int x1,int y1,int x2,int y2);
    def virtual void clearPointAndLine();
    def virtual void setBrushColor(int r,int g,int b);
    def virtual void clearString();
    def virtual bool setCircleRadius(int id,int radius);
    def virtual bool setLine(int id,int x1,int y1,int x2,int y2);
    def virtual bool setPointColor(int id);
    def virtual int  addString(string str,int x,int y);
    def virtual void clear();
    def virtual bool setCircleColor(int id);
    def virtual int  addCircle(int x,int y,int radius);
    def virtual int  addPoint(int x,int y);
    def virtual void redraw();
    def virtual bool setPoint(int id,int x,int y);
    def virtual bool setStringColor(int id);
    def virtual bool setCircle(int id,int x,int y);
}

struct UIGraphicProxy:UIGraphic{
    UIGraphic graphic;

    def this(UIGraphic g){
        this.graphic = g;
    }
    
    def override bool setString(int id,string arg1){
        return this.graphic.setString(id,arg1);
    }

    def override bool setLineColor(int id){
        return this.graphic.setLineColor(id);
    }

    def override bool setStringPosition(int id,int x,int y){
        return this.graphic.setStringPosition(id,x,y);
    }

    def override int addLine(int x1,int y1,int x2,int y2){
        return this.graphic.addLine(x1,y1,x2,y2);
    }

    def override void clearPointAndLine(){
        this.graphic.clearPointAndLine();
    }

    def override void setBrushColor(int r,int g,int b){
        this.graphic.setBrushColor(r,g,b);
    }

    def override void clearString(){
        this.graphic.clearString();
    }

    def override bool setCircleRadius(int id,int radius){
        return this.graphic.setCircleRadius(id,radius);
    }

    def override bool setLine(int id,int x1,int y1,int x2,int y2){
        return this.graphic.setLine(id,x1,y1,x2,y2);
    }

    def override bool setPointColor(int id){
        return this.graphic.setPointColor(id);
    }

    def override int addString(string str,int x,int y){
        return this.graphic.addString(str,x,y);
    }

    def override void clear(){
        this.graphic.clear();
    }

    def override bool setCircleColor(int id){
        return this.graphic.setCircleColor(id);
    }

    def override int addCircle(int x,int y,int radius){
        return this.graphic.addCircle(x,y,radius);
    }

    def override int addPoint(int x,int y){
        return this.graphic.addPoint(x,y);
    }

    def override void redraw(){
        this.graphic.redraw();
    }

    def override bool setPoint(int id,int x,int y){
        return this.graphic.setPoint(id,x,y);
    }

    def override bool setStringColor(int id){
        return this.graphic.setStringColor(id);
    }

    def override bool setCircle(int id,int x,int y){
        return this.graphic.setCircle(id,x,y);
    }
}

struct UIFlipGraphic:UIGraphicProxy{
    int pos;
    bool isHorizontal;

    def this(UIGraphic g,int pos,bool isHorizontal){
        super(g);
        this.pos = pos;
        this.isHorizontal = isHorizontal;
    }
    
    def override bool setStringPosition(int id,int x,int y){
        if(this.isHorizontal){
            x =  2 * this.pos  - x;
        } else {
            y =  2 * this.pos  - y;
        }
        return super.setStringPosition(id,x,y);
    }

    def override int addLine(int x1,int y1,int x2,int y2){
        if(this.isHorizontal){
            x1 =  2 * this.pos  - x1;
            x2 =  2 * this.pos  - x2;
        } else {
            y1 =  2 * this.pos  - y1;
            y2 =  2 * this.pos  - y2;
        }
        return super.addLine(x1,y1,x2,y2);
    }

    def override bool setLine(int id,int x1,int y1,int x2,int y2){
        if(this.isHorizontal){
            x1 =  2 * this.pos  - x1;
            x2 =  2 * this.pos  - x2;
        } else {
            y1 =  2 * this.pos  - y1;
            y2 =  2 * this.pos  - y2;
        }
        return super.setLine(id,x1,y1,x2,y2);
    }
    
    def override int addString(string str,int x,int y){
        if(this.isHorizontal){
            x =  2 * this.pos  - x;
        } else {
            y =  2 * this.pos  - y;
        }
        return super.addString(str,x,y);
    }

    def override int addCircle(int x,int y,int radius){
        if(this.isHorizontal){
            x =  2 * this.pos  - x;
        } else {
            y =  2 * this.pos  - y;
        }
        return super.addCircle(x,y,radius);
    }
    
    def override int addPoint(int x,int y){
        if(this.isHorizontal){
            x =  2 * this.pos  - x;
        } else {
            y =  2 * this.pos  - y;
        }
        return super.addPoint(x,y);
    }

    def override bool setPoint(int id,int x,int y){
        if(this.isHorizontal){
            x =  2 * this.pos  - x;
        } else {
            y =  2 * this.pos  - y;
        }
        return super.setPoint(id,x,y);
    }

    def override bool setCircle(int id,int x,int y){
        if(this.isHorizontal){
            x =  2 * this.pos  - x;
        } else {
            y =  2 * this.pos  - y;
        }
        return super.setCircle(id,x,y);
    }
}

struct UITransiteGraphic:UIGraphicProxy{
    Point offset;
    
    def this(UIGraphic g,Point offset){
        super(g);
        this.offset = offset;    
    }

    def override bool setStringPosition(int id,int x,int y){
        return super.setStringPosition(id,x+this.offset.x,y+this.offset.y);
    }

    def override int addLine(int x1,int y1,int x2,int y2){
        return super.addLine(x1+this.offset.x,y1+this.offset.y,x2+this.offset.x,y2+this.offset.y);
    }

    def override bool setLine(int id,int x1,int y1,int x2,int y2){
        return super.setLine(id,x1+this.offset.x,y1+this.offset.y,x2+this.offset.x,y2+this.offset.y);
    }
    
    def override int addString(string str,int x,int y){
        return super.addString(str,x+this.offset.x,y+this.offset.y);
    }

    def override int addCircle(int x,int y,int radius){
        return super.addCircle(x+this.offset.x,y+this.offset.y,radius);
    }
    
    def override int addPoint(int x,int y){
        return super.addPoint(x+this.offset.x,y+this.offset.y);
    }

    def override bool setPoint(int id,int x,int y){
        return super.setPoint(id,x+this.offset.x,y+this.offset.y);
    }

    def override bool setCircle(int id,int x,int y){
        return super.setCircle(id,x+this.offset.x,y+this.offset.y);
    }
}
//todo
struct UIRectCutGraphic:UIGraphicProxy{
    RectRegion region;
    
    def this(UIGraphic g,RectRegion region){
        super(g);
        this.region = region;    
    }
}

struct Point : Content;

struct UIComponent{
    UIComponent base;
    Point offset;
    Color color;
    
    def this(UIComponent base){
        this.base = base;
        this.offset = new Point(0,0);
        this.color = new Color(0,0,0);
    }

    def virtual UIComponent getBaseComponent(){
        return this.base;
    }

    def virtual void drawImpl(UIGraphic p);

    def void setColor(Color color){
        this.color = color;
    }

    def void draw(UIGraphic p){
        p.setBrushColor(this.color.r,this.color.g,this.color.b);
        this.drawImpl(p);
    }

    def virtual bool isCollisionWith(UIComponent p);
    def virtual bool contains(Point p){
        return false;
    }
    
    def virtual void onMouseClick(const int bid,const Point p){}
    
    def void mouseClick(const int bid,const Point p){
        if(this.contains(p)){
            this.onMouseClick(bid,p);
        }
    }

    def Point getOffsetPosition(){
        return this.offset;
    }

    def virtual Point getAbsolutionPosition(){
        auto base = this.getBaseComponent();
        if(base == null){
            return this.getOffsetPosition();
        }
        return this.getOffsetPosition() + base.getAbsolutionPosition();
    }
}

import"../container/bilist.xs";
import"../container/stream.xs";

struct UIComponentContent:Content{
    UIComponent component;
    Point offset;
    
    def this(UIComponent component,Point offset){
        this.component = component;
        this.offset = offset;
    }
    
    @string
    def override string toString(){
        return "null";
    }

}

struct UIComponentContainer:UIComponent{
    Sequence container;
    
    def this(UIComponent base){
        super(base);
        this.container = new List(); 
    }

    def void addComponent(UIComponent p,Point offset){
        p.base = this;
        p.offset = offset;
        this.container.add(new UIComponentContent(p,offset));
    }

    def override void drawImpl(const UIGraphic p){
        this.container.forEach(new Consumer$(c)->{
            const auto ui =((UIComponentContent) c);
            ui.component.draw(new UITransiteGraphic(p,ui.offset));
        });
    }
    def override void onMouseClick(const int bid,const Point p){
        this.container.stream()
			.filter(new Consumer$(c) -> new BoolContent(((UIComponentContent)c).component.contains(p)))
			.forEach(new Consumer$(c) -> {
				new Future(new AsyncRunnable() {
					def override Content get() {
						((UIComponentContent)c).component.onMouseClick(bid, p);
					}
				});
			});
    }
    
    def override bool isCollisionWith(const UIComponent p){
        return this.container.stream().anyMatch(new Consumer$(c)->new BoolContent(((UIComponentContent)c).component.isCollisionWith(p)));
    }
    
    def override bool contains(Point p){
        return this.container.stream().anyMatch(new Consumer$(c)->new BoolContent(((UIComponentContent)c).component.contains(p)));
    }
}

struct UIRectComponent:UIComponent{
    RectRegion region;
    int id0,id1,id2,id3;

    def this(Point o,int width,int height){
        super(null);
        this.region = new RectRegion(o,width,height);
        this.id0 = -1;
    }

    def override void drawImpl(const UIGraphic p){
        const int x = this.region.o.x,y = this.region.o.y,
                  x2 = x + this.region.width,y2 = y + this.region.height;
        if(this.id0 < 0){
            this.id0 = p.addLine(x, y, x2,y);
            this.id1 = p.addLine(x, y, x, y2);
            this.id2 = p.addLine(x2,y, x2,y2);
            this.id3 = p.addLine(x, y2,x2,y2);
        } else {
            p.setLine(this.id0,x,y,x2,y);
            p.setLine(this.id1,x,y,x,y2);
            p.setLine(this.id2,x2,y,x2,y2);
            p.setLine(this.id3,x,y2,x2,y2);
            p.setLineColor(this.id0);
            p.setLineColor(this.id1);
            p.setLineColor(this.id2);
            p.setLineColor(this.id3);
        }
    }

    def override bool contains(Point p){
        auto region1 = new RectRegion(this.getAbsolutionPosition().add(this.region.o),this.region.width,this.region.height);        
        return region1.contains(p);
    }

    def override bool isCollisionWith(const UIComponent p){
        if(p instanceof UIRectComponent){
            const auto f = (UIRectComponent)p;
            auto region1 = new RectRegion(this.getAbsolutionPosition(),this.region.width,this.region.height);
            auto region2 = new RectRegion(f.getAbsolutionPosition(),f.region.width,f.region.height);
            return region1.isCollisionWith(region2);
        }
        return p.isCollisionWith(this);
    }
}

struct UITextRectComponent : UIRectComponent{
    int string_id;
	string content;

    def this(Point o, int width, int height, string content){
        super(o, width, height);
		this.string_id = -1;
		this.content = content;
    }

    def override void drawImpl(const UIGraphic p){
		super.drawImpl(p);
		const int x = this.region.o.x, y = this.region.o.y,
            x2 = x + this.region.width, y2 = y + this.region.height;
		if (this.string_id > 0) {
			p.setStringPosition(this.string_id, x, y);
			p.setStringColor(this.string_id);
		} else {
			this.string_id = p.addString(this.content, x, y);
		}
    }
}

struct UIRoundComponent:UIComponent{
    RoundRegion region;
    int id;
    
    def this(Point o, int radius){
        super(null);
        this.region = new RoundRegion(o,radius);
        this.id = -1;
    }
    
    def override void drawImpl(UIGraphic p){
        if(this.id < 0){
            this.id = p.addCircle(this.region.o.x-this.region.radius,this.region.o.y-this.region.radius,this.region.radius*2);
        } else {
            p.setCircle(this.id,this.region.o.x-this.region.radius,this.region.o.y-this.region.radius);
            p.setCircleRadius(this.id,this.region.radius*2);
            p.setCircleColor(this.id);
        }
    }

    def override bool contains(Point p){
        auto region1 = new RoundRegion(this.getAbsolutionPosition().add(this.region.o),this.region.radius);
        return region1.contains(p);
    }

    def override bool isCollisionWith(UIComponent p){
        if(p instanceof UIRoundComponent){
            const auto f = (UIRoundComponent)p;
            auto region1 = new RoundRegion(this.getAbsolutionPosition(),this.region.radius);
            auto region2 = new RoundRegion(f.getAbsolutionPosition(),f.region.radius);
            return region1.isCollisionWith(region2);
        } else if( p instanceof UIRectComponent){
            const auto f = (UIRectComponent)p;
            auto region1 = new RoundRegion(this.getAbsolutionPosition(),this.region.radius);
            auto region2 = new RectRegion(f.getAbsolutionPosition(),f.region.width,f.region.height);
            return region1.isCollisionWith(region2);
        }
        return p.isCollisionWith(this);
    }
}

struct PaintPadGraphic : UIGraphic{
    PaintPad pad;
    //init function
    def this(PaintPad p){
        this.pad = p;
    }
    //functions
    def override bool setString(int id,string arg1){
        this.pad.setString(id,arg1);
    }

    def override bool setLineColor ( int id ) {
        return this.pad.setLineColor(id);
    }
    def override bool setStringPosition ( int id,int x,int y ) {
        return this.pad.setStringPosition( id, x, y);
    }
    def override int addLine ( int x1,int y1,int x2,int y2 ) {
        return this.pad.addLine( x1, y1, x2, y2);
    }
    def override void clearPointAndLine() {
        this.pad.clearPointAndLine();
    }
    def override void setBrushColor ( int r,int g,int b ) {
        this.pad.setBrushColor( r, g, b);
    }
    def override void clearString(){
        this.pad.clearString();
    }
    def override bool setCircleRadius(int id,int radius ) {
        return this.pad.setCircleRadius(id,radius);
    }
    def override bool setLine(int id,int x1,int y1,int x2,int y2) {
        return this.pad.setLine(id,x1,y1,x2,y2);
    }
    def override bool setPointColor(int id) {
        return this.pad.setPointColor(id);
    }
    def override int addString(string str,int x,int y){
        return this.pad.addString(str,x,y);
    }
    def override void clear(){
        this.pad.clear();
    }
    def override bool setCircleColor(int id){
        return this.pad.setCircleColor(id);
    }
    def override int addCircle(int x,int y,int radius){
        return this.pad.addCircle(x,y,radius);
    }
    def override int addPoint(int x,int y){
        return this.pad.addPoint(x,y);
    }
    def override void redraw(){
        this.pad.redraw();
    }
    def override bool setPoint(int id,int x,int y){
        return this.pad.setPoint(id,x,y);
    }
    def override bool setStringColor(int id){
        return this.pad.setStringColor(id);
    }
    def override bool setCircle(int id,int x,int y){
        return this.pad.setCircle(id,x,y);
    }
    
    def void wait(){
        this.pad.wait();
    }
    
    def void show(){
        this.pad.show();
    }
};

struct UIBaseComponent:UIComponentContainer{
    PaintPadGraphic g;
    def this(string title, int x, int y){
        super(null);
        const auto this_ = this;
        this.g = new PaintPadGraphic(new PaintPad(title,x, y){
            def override void onMouseClick(int bid, int x, int y){
                this_.onMouseClick(bid, new Point(x, y));
            }
        });
    }
    def override void onMouseClick(int bid,Point p){
        super.onMouseClick(bid, p);
        this.draw(this.g);
        this.g.redraw();
    }
    def void show(){
        this.draw(this.g);
        this.g.show();
    }
    
    def void wait(){
        this.g.wait();
    }
}

if(false||_isMain_){
    auto c = new UIBaseComponent("test", 600, 800);
    c.addComponent(new UIRectComponent(new Point(200,200),100,100){
        def override void onMouseClick(const int bid,const Point p){
            this.setColor(new Color(rand()%255,rand()%255,rand()%255));
            println("rect1");
        }
    },new Point(100,100));
    c.addComponent(new UIRectComponent(new Point(200,200),100,100){
        def override void onMouseClick(const int bid,const Point p){
            this.setColor(new Color(rand()%255,rand()%255,rand()%255));
            println("rect2");
        }
    },new Point(0,0));
	c.addComponent(new UITextRectComponent(new Point(0, 0), 100,100, "test"), new Point(0, 0));
    c.addComponent(new UIRoundComponent(new Point(200,200),100){
        def override void onMouseClick(const int bid,const Point p){
            this.setColor(new Color(rand()%255,rand()%255,rand()%255));
            println("round");
        }
    },new Point(100,100));
    c.show();
    c.wait();
}