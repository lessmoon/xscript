import  "lib/file.xs";
import  "lib/system.xs";
import  "lib/concurrent.xs";
import  "math/Math.xs";
import  "math/Ratio.xs";
import  "parser/parser.xs";
import  "container/darray.xs";
import  "container/hashmap.xs";
import  "container/priorityqueue.xs";
import  "ui/paintpad.xs";
import  "ui/cyclepaintpad.xs";
import  "ui/uicomponent.xs";
import  "rpg/parser.xs";
import  "reflection/reflect.xs";
import  "lib/keymap.xs";
import  "parser/json.xs";
import "math/expression.xs";

/* struct UnaryOperator<ReturnT: Value, ParamT: Value> {
    def default virtual ReturnT calculate(ParamT v);
}

struct HashMap<KeyType: HashKey, ValueType: Value> {
    def default virtual ValueType get(KeyType key);
}
{
    auto condition = new UnaryOperator<Integer, Bool>^x->new Bool(x>0);
    auto list = new List<Integer>();
    list.stream().filter(new UnaryOperator<Integer, Bool>^x->new Bool(x>0)).reduce();
    //| map | HashMap | String, Integer |
    auto x = map.get(value);
    => x = (Integer)(map.get(value))
       
} */

struct Shape {
    def virtual void draw(PaintPad pad, Point offset);
}

def RealPoint getNormalVector(RealPoint p) {
	return new RealPoint(-p.y, p.x);
}

def RealPoint normalize(RealPoint p) {
	real norm = p.x*p.x + p.y*p.y;
	if (norm > -0.000001 && norm < 0.000001) {
		return p;
	} else {
		norm = sqrt(norm);
		return new RealPoint(p.x / norm, p.y / norm);
	}
}

def void calcCollision(RealPoint collisionPlane, RealPoint v1, RealPoint v2,
						RealPoint v1_2, RealPoint v2_2, 
						real mass1, real mass2, real ratio) {
	RealPoint x_axis = getNormalVector(collisionPlane);
	RealPoint y_axis = collisionPlane;
	//rotation
	RealPoint v1_v = new RealPoint(x_axis.dot(v1), y_axis.dot(v1));
	RealPoint v2_v = new RealPoint(x_axis.dot(v2), y_axis.dot(v2));
	real v1_x = ratio*((mass1-mass2)*v1_v.x + 2*mass2*v2_v.x)/(mass1+mass2);
	//real v1_y = ((mass1-mass2)*v1_v.y + 2*mass2*v2_v.y)/(mass1+mass2);
	real v2_x = ratio*((mass2-mass1)*v2_v.x + 2*mass1*v1_v.x)/(mass1+mass2);
	//real v2_y = ((mass2-mass1)*v2_v.y + 2*mass1*v1_v.y)/(mass1+mass2);
	v1_2.x = (x_axis.x * v1_x + y_axis.x * v1_v.y);
	v1_2.y = (x_axis.y * v1_x + y_axis.y * v1_v.y);
	v2_2.x = (x_axis.x * v2_x + y_axis.x * v2_v.y);
	v2_2.y = (x_axis.y * v2_x + y_axis.y * v2_v.y); 
}

def RealPoint fixVelocity(RealPoint pos, RealPoint velocity, int width, int height, int radius, int cell, real ratio) {
	RealPoint v = new RealPoint(velocity.x, velocity.y);
	if ((pos.x <= radius+cell && velocity.x < 0) || (pos.x >= width-radius-cell && velocity.x > 0)) {
		v.x = -v.x*ratio;
	}
	if ((pos.y <= radius+cell && velocity.y < 0) || (pos.y >= height-radius-cell && velocity.y > 0)) {
		v.y = -v.y*ratio;
	}
	return v;
}

def void reduceVelocity(RealPoint velocity, real ratio) {
	velocity.x *= ratio;
	velocity.y *= ratio;
}

struct CollisionDemo : CyclePaintPad {
    int[] cid;

	int[] lid;//for v1

 
    RoundRegion[] round;
	RealPoint[] v;
	RealPoint[] pos;
	
    def this(){
        super(new PaintPad("CollisionDemo", 600, 600), 1);
		
		this.pos = new RealPoint[]{new RealPoint(300, 100), new RealPoint(300, 200), new RealPoint(300, 400)};
		this.round = new RoundRegion[]{new RoundRegion(this.pos[0].toPoint(), 20),
						new RoundRegion(this.pos[1].toPoint(), 20),
						new RoundRegion(this.pos[2].toPoint(), 20)};
		
		this.v = new RealPoint[]{new RealPoint(0.0, 0.0), new RealPoint(0.0, 0.0), new RealPoint(0.0, 0.6)};
		
		this.cid = new int[sizeof(this.pos)];
		for (int i = 0; i < sizeof(this.pos); i++) {
			this.cid[i] = this.pad.addCircle(this.round[i].o.x-this.round[i].radius, 
									   this.round[i].o.y-this.round[i].radius, 
									   this.round[i].radius*2);
		}
		
		this.pad.setBrushColor(GREEN_COLOR.r, GREEN_COLOR.g, GREEN_COLOR.b);
		this.lid = new int[sizeof(this.pos)];
		for (int i = 0; i < sizeof(this.pos); i++) {
			this.lid[i] = this.pad.addLine(this.round[i].o.x, this.round[i].o.y, 
							 this.round[i].o.x + this.v[i].x*20, 
							 this.round[i].o.y + this.v[i].y*20);
		}

        this.pad.setBrushColor(BLUE_COLOR.r, BLUE_COLOR.g, BLUE_COLOR.b);

        this.pad.addLine(10, 10, 10, 590);
		this.pad.addLine(10, 10, 590, 10);
		this.pad.addLine(10, 590, 590, 590);
        this.pad.addLine(590, 10, 590, 590);
    }
    
    def override void run(){
		for (int i = 0; i < sizeof(this.pos); i++) {
			this.pos[i] = this.pos[i] + this.v[i];
			this.round[i].o = this.pos[i].toPoint();
		}

        //collision detection
		RealPoint[] newVelocity = new RealPoint[sizeof(this.pos)];
		for (int i = 0; i < sizeof(this.pos); i++) {
			newVelocity[i] = fixVelocity(this.pos[i], this.v[i], 600, 600, this.round[i].radius, 10, 1);
		}

		for (int i = 0; i < sizeof(this.pos); i++) {
			for (int j = i+1; j < sizeof(this.pos); j++) {
				if (this.round[i].isCollisionWith(this.round[j])) {
					auto c = this.pos[i].sub(this.pos[j]);
					auto h = getNormalVector(c);
					this.pad.setBrushColor(RED_COLOR.r, RED_COLOR.g, RED_COLOR.b);
					this.pad.setCircleColor(this.cid[i]);
					this.pad.setCircleColor(this.cid[j]);
					calcCollision(normalize(h), newVelocity[i], newVelocity[j],
								  newVelocity[i], newVelocity[j], 
								  this.round[i].radius*this.round[i].radius, this.round[j].radius*this.round[j].radius, 
								  1);
					println("collision[" + i + " ,"+ j + "] = " + newVelocity[i].toString() + " " + newVelocity[j].toString());
					this.pad.setBrushColor(BLACK_COLOR.r, BLACK_COLOR.g, BLACK_COLOR.b);

					new Thread(new Schedule(500, new OnceScheduce, new Runnable->{
						this.pad.setCircleColor(this.cid[i]);
						this.pad.setCircleColor(this.cid[j]);
						this.pad.redraw();
					})).start();
				}
			}
		}
		
		for (int i = 0; i < sizeof(this.pos); i++) {
			this.v[i] = newVelocity[i];
			reduceVelocity(this.v[i], 1);
			this.pad.setCircle(this.cid[i], this.pos[i].x-this.round[i].radius, 
								this.pos[i].y-this.round[i].radius);
		
			this.pad.setLine(this.lid[i], this.round[i].o.x, this.round[i].o.y,
							 this.round[i].o.x + this.v[i].x*20, this.round[i].o.y + this.v[i].y*20);
		
		}

        super.run();
    }
    
    def void open(){
        this.pad.show();
    }

    def void wait(){
        this.pad.wait();
		this.stop();
    }
}

{
	CollisionDemo game = new CollisionDemo();
	game.open();
	game.start();
	game.wait();
}

struct Triangle {
    Point a, b, c;
    PaintPad pad;
    int lid_a, lid_b, lid_c;
    
    def this(Point a, Point b, Point c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.pad = null;
        this.lid_a = 0;
        this.lid_b = 0;
        this.lid_c = 0;
    }
    
    def void setPaintPad(PaintPad pad) {
        this.pad = pad;
        this.lid_a = this.pad.addLine(this.a.x, this.a.y, this.b.x, this.b.y);
        this.lid_b = this.pad.addLine(this.a.x, this.a.y, this.c.x, this.c.y);
        this.lid_c = this.pad.addLine(this.c.x, this.c.y, this.b.x, this.b.y);
    }
    
    def void redraw(Point offset) {
        this.a = this.a + offset;
        this.b = this.b + offset;
        this.c = this.c + offset;
        this.pad.setLine(this.lid_a, this.a.x, this.a.y, this.b.x, this.b.y);
        this.pad.setLine(this.lid_b, this.a.x, this.a.y, this.c.x, this.c.y);
        this.pad.setLine(this.lid_c, this.c.x, this.c.y, this.b.x, this.b.y);
        this.pad.redraw();
    }
};
const int LEFT = 0,
          RIGHT = 1,
          UP    = 2,
          DOWN  = 3;
          
def int oppositeDirection(int dir) {
    switch (dir) {
    case LEFT:
        return RIGHT;
    case RIGHT:
        return LEFT;
    case UP:
        return DOWN;
    case DOWN:
        return UP;
    }
    return -1;
    
}

struct Rect {
    int lid_0;
    int lid_1;
    int lid_2;
    int lid_3;
    PaintPad pad;
    Point a;
    Point b;
    Color color;
    
    def this(PaintPad pad, Point a, Point b) {
        this.pad = pad;
        this.lid_0 = pad.addLine(a.x, a.y, a.x, b.y);
        this.lid_1 = pad.addLine(a.x, a.y, b.x, a.y);
        this.lid_2 = pad.addLine(b.x, a.y, b.x, b.y);
        this.lid_3 = pad.addLine(a.x, b.y, b.x, b.y);
        this.a = a;
        this.b = b;
        this.color = BLACK_COLOR;
    }
    
    def void refresh() {
        this.pad.setBrushColor(this.color.r, this.color.g, this.color.b);
        this.pad.setLineColor(this.lid_0);
        this.pad.setLineColor(this.lid_1);
        this.pad.setLineColor(this.lid_2);
        this.pad.setLineColor(this.lid_3);
        this.pad.setLine(this.lid_0, this.a.x, this.a.y, this.a.x, this.b.y);
        this.pad.setLine(this.lid_1, this.a.x, this.a.y, this.b.x, this.a.y);
        this.pad.setLine(this.lid_2, this.b.x, this.a.y, this.b.x, this.b.y);
        this.pad.setLine(this.lid_3, this.a.x, this.b.y, this.b.x, this.b.y);
    }
    
    def void setColor(Color color) {
        this.color = color;
        this.refresh();
    }

    def void moveTo(Point a, Point b) {
        this.a = a;
        this.b = b;
        this.refresh();
    }
    
    def void move(Point offset) {
        this.moveTo(this.a + offset,
                    this.b + offset);
    }

    def void remove() {
        this.pad.removeLine(this.lid_0);
        this.pad.removeLine(this.lid_1);
        this.pad.removeLine(this.lid_2);
        this.pad.removeLine(this.lid_3);
        this.pad = null;
    }
}

struct SnakeBodyNode : Content{
    Rect bodyView;
    Point point;
    
    def this(PaintPad pad, Point position) {
        this.bodyView = new Rect(pad, new Point(position.x * 50 + 2, position.y * 50 + 2), new Point(position.x * 50 + 48, position.y * 50 + 48));
        this.point = position;
    }
    
    def void moveTo(Point position) {
         this.point = position;
         this.bodyView.moveTo(new Point(position.x * 50 + 2, position.y * 50 + 2),
                              new Point(position.x * 50 + 48, position.y * 50 + 48));
    }
    
    def void move(int direction) {
        const Point[] offset = {new Point(-1, 0), new Point(1, 0), new Point(0, -1), new Point(0, 1)};
        this.moveTo(this.point + offset[direction]);
    }
    
    def void setColor(Color color) {
        this.bodyView.setColor(color);
    }
    
    @string
    def override string toString() {
        return this.point.toString();
    }
}

struct Snake {
    int speed;
    List body;
    int direction;
    int lastDirection;
    PaintPad pad;
    bool[][] occMap;
    
    def this(int direction, int speed, List body, PaintPad pad, bool[][] occMap) {
        this.direction = direction;
        this.speed = speed;
        this.body = body;
        this.pad = pad;
        this.lastDirection = direction;
        this.occMap = occMap;
        body.forEach(new Consumer$bodyNode->{
            const auto node = (SnakeBodyNode) bodyNode;
            occMap[node.point.x][node.point.y] = true;
        });
    }
    
    def void move(bool hasApple) {
        SnakeBodyNode newHead = null;
        const auto head = (SnakeBodyNode)this.body.front().value;
        if (hasApple) {
            auto p = new Point(head.point.x, head.point.y);
            newHead = new SnakeBodyNode(this.pad, p);
            newHead.move(this.direction);
        } else {
            newHead = (SnakeBodyNode)this.body.pop_back();
            this.occMap[newHead.point.x][newHead.point.y] = false;
            newHead.moveTo(head.point);
            newHead.move(this.direction);
        }
        head.setColor(BLACK_COLOR);
        newHead.setColor(RED_COLOR);
        this.body.push_front(newHead);
        this.occMap[newHead.point.x][newHead.point.y] = true;
    }

    def void newRound() {
        this.lastDirection = this.direction;
    }
    
    def void setDirection (int direction) {
        this.direction = direction;
    }
    
    def bool isDirectionOk(int direction) {
        if (direction == oppositeDirection(this.lastDirection)) {
            return false;
        }
        const auto head = (SnakeBodyNode)this.body.front().value;
        int x = head.point.x;
        int y = head.point.y;
        switch(direction) {
        case LEFT:
            if (x == 0) {
                return false;
            }
            x--;
            break;
        case RIGHT:
            if (x == sizeof this.occMap - 1) {
                return false;
            }
            x++;
            break;
        case UP:
            if (y == 0) {
                return false;
            }
            y--;
            break;
        case DOWN:
            if (y == sizeof this.occMap[0] - 1) {
                return false;
            }
            y++;
            break;
        }
        return !this.occMap[x][y];
    }
    
    @string
    def string toString() {
        return this.body.toString();
    }
    
    def void redraw() {
        this.pad.redraw();
    }
}

{
    const int WIDTH = 12,
              HEIGHT = 12;
    PaintPad pad = new PaintPad("snake", 50*WIDTH, 50*HEIGHT);
    bool[][] occMap = new bool[][WIDTH];
    for (int i = 0; i < WIDTH; i++) {
        occMap[i] = new bool[HEIGHT];
    }
    
    srand(time());
    
    List a = new List();
    a.add(new SnakeBodyNode(pad, new Point(5, 3)));
    a.add(new SnakeBodyNode(pad, new Point(6, 3)));
    a.add(new SnakeBodyNode(pad, new Point(7, 3)));
    a.add(new SnakeBodyNode(pad, new Point(8, 3)));
    a.add(new SnakeBodyNode(pad, new Point(9, 3)));
    const auto snake = new Snake(RIGHT, 0, a, pad, occMap);

    const auto timer = new Timer2Adapter(100) {
        def override void run() {
            int dir;
            do {
                dir = rand() % 4;
                println("try to dir:" + dir);
            } while (!snake.isDirectionOk(dir));
            println("decide to dir:" + dir);
            snake.setDirection(dir);
            snake.move(false);
            snake.redraw();
            snake.newRound();
        }
    };
    timer.start();
    pad.show();
    pad.wait();
    timer.stop();
}


{
    const Point LEFT = new Point(-10, 0),
                RIGHT = new Point(10, 0),
                UP    = new Point(0, -10),
                DOWN = new Point(0, 10);
    const auto radius_a = 60,
               radius_b = 40;
    auto pad = new PaintPad("Space Invader", 600, 600) {
        Triangle plane;
        int id;
        int id_2;
        Point a;
        Point b;
        int score_id;
        int score;
        
        def this(string name, int width, int height) {
            super(name, width, height);
            this.a = new Point(rand()%300, 80);
            this.b = new Point(rand()%300 + 300, 40);
            this.plane = new Triangle(new Point(280, 580), new Point(320, 580), new Point(300, 540));
            this.plane.setPaintPad(this);
            this.score = 0;
            this.id = this.addCircle(this.a.x, this.a.y, 50);
            this.id_2 = this.addCircle(this.b.x, this.b.y, 80);
            this.setFont(new Font("heiti", 0, 32));
            this.setBrushColor(255, 0, 0);
            this.score_id = this.addString("0", 50, 50);
            this.setBrushColor(0, 0, 0);
            auto _this = this;
            (new Timer2Adapter(5) {
                def this(int interval) {
                    super(interval);
                }
                
                def override void run() {
                    if (_this.a.y >= 600) {
                        _this.a.y = 0;
                        _this.a.x = rand()%300;
                    }
                    if (_this.b.y >= 600) {
                        _this.b.y = 0;
                        _this.b.x = rand()%300 + 300;
                    }
                    _this.a.y += 1;
                    _this.b.y += 1;
                    _this.setCircle(_this.id, _this.a.x, _this.a.y);
                    _this.setCircle(_this.id_2, _this.b.x, _this.b.y);
                    _this.redraw();
                }
            }).start();
        }
        
        def void refreshScore(int score) {
            this.setString(this.score_id, score);
        }
        
        def override void onPress(int kid) {
            Point dir;
            switch (kid) {
            case VK_A: case VK_LEFT:
                dir = LEFT;
                break;
            case VK_D: case VK_RIGHT:
                dir = RIGHT;
                break;
            case VK_W: case VK_UP:
                dir = UP;
                break;
            case VK_S: case VK_DOWN:
                dir = DOWN;
                break;
            case VK_SPACE: case VK_J:
                auto _this = this;
                //add a bullet
                auto id = this.addCircle(this.plane.c.x-5, this.plane.c.y, 10);
                (new Timer2Adapter(10, this.plane.c.x-5, this.plane.c.y) {
                    int ox;
                    int oy;
                    def this(int interval, int x, int y) {
                        super(interval);
                        this.ox = x;
                        this.oy = y;
                    }
                    
                    def override void run() {
                        if (this.oy <= 0) {
                            _this.removeCircle(id);
                            _this.redraw();
                            this.stop();
                            return;
                        }
                        this.oy -= 10;
                        //x,y a,b
                        //collision detection
                        int x = this.ox + 5,
                            y = this.oy + 5,
                            x1= _this.a.x + radius_a/2,
                            y1= _this.a.y + radius_a/2;

                        int r2 = (radius_a/2 + 5) * (radius_a/2 + 5);
                        int z = (x1-x)*(x1-x) + (y1-y)*(y1-y);
                        if (z <= r2) {
                            _this.removeCircle(id);
                            _this.a.y = 0;
                            _this.a.x = rand()%300;
                            _this.score += 100;
                            _this.refreshScore(_this.score);
                            _this.redraw();
                            this.stop();
                            return;
                        }
                        _this.setCircle(id, this.ox, this.oy);
                        _this.redraw();
                    }
                }).start();
                return;
            default:
                return;
            }
            this.plane.redraw(dir);
        }
    };
    pad.show();
    pad.wait();
}
if(false)
{
    //blink
    auto pad = new PaintPad("blink", 200, 200) {
        StringBuffer buf;
        int str_id;
        int lid;
        Thread t;
        
        def this(string name, int width, int height) {
            super(name, width, height);
            this.buf = new StringBuffer();
            this.str_id = this.addString(this.buf.toString(), 50, 50);
            this.lid = this.addLine(50, 40, 50, 50);
            auto b = new bool[]{true};
            auto _this = this;
            this.t = new Thread(new Schedule(500, new RepeatSchedule, new Runnable->{
                if (b[0]) {
                    _this.setBrushColor(255, 255, 255);
                } else {
                    _this.setBrushColor(0, 0, 0);
                }
                _this.setLineColor(this.lid);
                const int len = this.buf.length();
                _this.setLine(this.lid, 50 + len*6, 40, 50 + len*6, 50);
                b[0] = !b[0];
                _this.redraw();
            }));
        }
        
        def override void show() {
            this.t.start();
            super.show();
        }
        
        def override void onPress(int kid) {
            if (kid >= VK_A&&kid <= VK_Z) {
                this.buf.appendCharacter((char)('a' + (kid - VK_A)));
            } else if (kid == VK_BACK_SPACE) {
                const int len = this.buf.length();
                if (len == 0) {
                    return;
                }
                this.buf.delete(len-1, len);
            }
            this.setString(this.str_id, this.buf.toString());
            this.redraw();
        }
        
        def override void onClose() {
            this.t.interrupt();
            super.onClose();
        }
    };
    pad.show();
    pad.wait();
}

struct base;
struct base {
    def default virtual base getBase();
}

{
    auto a = new base {
        def override base getBase() {
            return null;
        }
    };
    
}
{
    RPGParser p = new RPGParser();
    RPGRuntime r = new RPGRuntime(p);
    r.registerFunction("sleep",new Sleep);
    r.registerFunction("condition",new Cond);
    r.registerFunction("jump",new Jump);
    r.registerFunction("choice",new Choice);
    r.registerFunction("set",new Set);
    r.registerFunction("print",new Print);
    r.registerFunction("type",new StopPrint);
    r.registerFunction("open",new Open);
    r.registerFunction("add",new RPGAdd);
    r.registerFunction("read",new TypeString);
    r.registerFunction("case",new RPGCase);
    r.registerFunction("time",new RPGTime);
    r.registerFunction("checkpoint", new Function$(r,args)->{
        const auto fio = new FileOutputStream(new SimpleFile(args[0]), false);
        JSONObject root = new JSONObject();
        JSONObject vars = new JSONObject();
        root.insert("variables", vars);
        r.varMap.iterator().stream().forEach(new Consumer$x->{
            vars.insert(((HashPair)x).key.toString(), new JSONString(((HashPair)x).value.toString()));
        });
        /*.sort(new Comparator$(x1,x2)->{
            auto x1_c = ((HashPair)x1).key.toString();
            auto x2_c = ((HashPair)x2).key.toString();
            return x1_c < x2_c?1:x1_c == x2_c?0:-1;
        }).forEach(new Consumer$x->{
            fio.writeString(x.toString() + "\r\n");
        });*/
        root.insert("filename", new JSONString(r.filename));
        root.insert("cursor", new JSONNumber(r.index));
        fio.writeString(root.toString());
        fio.close();
    });
    r.open("test");
    r.run();
}

{
    auto fio = new FileInputStream(new SimpleFile("test.json"));
    auto l = new JSONLexer(new CharInputStream(){
        def override int next(){
            return fio.readChar();
        }
    });

    auto p = new JSONParser(l);
    println(p.parse());
    fio.close();
}

int[] r = {3243, 545};

int x = 80, y = 170, width = 40;

struct MyPaintPad:PaintPad{
    def this(){
        super("test",200,200);
        Font f = super.getFont();
    }
    
    def override void onClick(int bid){
        super.onClick(bid);
        switch(bid){
        case VK_LEFT:case VK_A:
           x-=10;
           break;
        case VK_RIGHT:case VK_D:
           x+=10;
           break;
        case VK_ESCAPE:
           this.close();
           break;
        default:
           println(bid);
        }
    }
    
    def override void onPress(int bid){
        this.onClick(bid);
    }
}

struct Game : CyclePaintPad {
    int cid;
    real x;
    real y;
    real v_x;
    real v_y;
    int lid;
    
    def this(){
        super(new MyPaintPad(),1);
        this.cid = this.pad.addCircle(50,50,10);
        this.pad.setBrushColor(0,255,255);
        this.lid = this.pad.addLine(x,y,width+x,y);
       
        this.pad.setBrushColor(0,0,255);
        this.pad.addLine(10,10,10,190);
        this.pad.addLine(10,10,190,10);
        this.pad.addLine(10,190,190,190);
        this.pad.addLine(190,10,190,190);
        this.v_x = 0.05;
        this.v_y = 0.1;
        this.x = 55;
        this.y = 55;
    }
    
    def override void run(){
        this.x += this.v_x;
        this.y += this.v_y;
        //collision detection
     
        if(this.x <= 5 || this.x >= 185){
            this.v_x = -this.v_x;
        }
        if(this.y <= 5 || this.y >= 185){
            this.v_y = -this.v_y;
        }
        if( x < 10 ){
           x = 10;
        }
        if( x > 150){
           x = 150;
        }
        
        if(this.y >= y && this.x >= x && this.x <= x + width){
            this.v_y = -this.v_y;
        }
        
        this.pad.setLine(this.lid,x,y,width+x,y);
        this.pad.setCircle(this.cid,this.x,this.y);
        super.run();
    }
    
    def void open(){
        this.pad.show();
    }

    def void wait(){
        this.pad.wait();
    }
}

struct baseA{
    int id;
    
    def void init(int id){
        this.id = id;
    }
    
    @string
    def virtual string who(){
        return ("baseA:" + this.id);
    }
}

struct baseB:baseA{
    def this(int i){
        super.init(i);
    }
    
    def override string who(){
        return "baseB:" + super.who();
    }
}

struct deriveC:baseB{
    def this(int i){
        super.init(i);
    }

    def override string who(){
        return "baseC:" + super.who();
    }
}

struct MyXPaintPad:PaintPad{
    int lastx,lasty;

    def this(string name,int width,int height){
        super(name,width,height);
    }

    def override void onMouseClick(int code,int x,int y){
         println("(" + x +","+ y + ")");

         if(code == 1){
            this.addPoint(x,y);
         } else {
            this.addLine(x,y,this.lastx,this.lasty);
         }
         this.lastx = x;
         this.lasty = y;
         this.redraw();
    }

    def override void onClose(){
        println("closed");
        this.close();
    }

    def override void onClick(int code){
        println(code);
    }
}

struct PrintCount : Runnable{
    int i;
    def this(){
        this.i = 0;
    }
    def override void run(){
        println(this.i ++);
    }
}

AtomicInteger value = new AtomicInteger();

struct PairContent2 : Content {
    int id;
    int value;
    
    def this(int id,int value){
        this.id = id;
        this.value = value;
    }
    
    def override string toString(){
        return "" + this.id + ":" + this.value;
    }
}

ConcurrentQueue queue = new ConcurrentQueue(100);

struct PrintNumber : Runnable {
    int id ;

    def this(int id){
        this.id = id;
    }
    
    def override void run(){
        int v = 0;
        while((v = value.getAndIncrement()) < 100 ){
            queue.put(new PairContent2(this.id,v));
            sleep(50);
        }
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
    println("Test for UI the Game");
    auto game = new Game();
    game.open();
    game.start();
    game.wait();
}

{
    println("Test for Anonymous Inner Struct");
    Sequence x = new List();
    for(int i = 0;i < 100;i++){
        x.add(new IntContent(rand()%100));
    }
    println("Test");
    x.forEach(new Consumer {
        def override Content apply(Content i){
            print( " " + i);
        }
    });
    println("");

    new RangeStream(1,100)
        .filter(new Consumer$i->new BoolContent(0==((int)(IntContent)i)%4))
        .forEach(new Consumer$i->{println(i);});

    println("\nTest2");
    x.stream()
     .filter(new Consumer$(i)->new BoolContent((((IntContent)i).val % 4) == 0))
     .map(new Consumer$(i)->new IntContent(((IntContent)i).val * 4))
     .sort(new Comparator$(a,b)->((IntContent)b).val - ((IntContent)a).val)
     .forEach(new Consumer$(i)->{print( " " + i );});

    println("\nTest2 end");
}

{
    println("Test for multi thread");
    Thread t1 = new Thread(new PrintNumber(1));
    Thread t2 = new Thread(new PrintNumber(2));
    Thread t3 = new Thread(new PrintNumber(3));
    t1.start();
    t2.start();
    t3.start();
    int i =0;
    while(true){
        println(queue.pop());
        if(i ++ == 20){
            t1.interrupt();
        }
        if(i > 99){
            break;
        }
    }
}

{
   println("Test for HashMap");
   HashMap hm = new HashMap();
   for(int i = 0; i < 20;i++){
        hm.set(new StringHashContent(""+rand()),new IntContent(rand()));
        println("" + i + "\n" + hm);
   }
}

struct JustOnce:Runnable{
    def override void run(){
        Thread t = getCurrentThread();
        for(int i =0;i < 10;i++){
            print("[" + t.getThreadId() + "]:" + i + " will stop\n");
            t.interrupt();
        }
    }
}

/*
{
    println("Test for Thread type");
    JustOnce i = new JustOnce;
    Thread t = new Thread(i);
    int beg = time();
    print(beg);
    t.start();
    t.join(0);
    int end = time();
    print(end);
    print("join time:"+(end-beg)+"\n");
}
*/
real PI = 3.141592654;

def void drawHand(PaintPad p,int v,real theta,int len,int r,int g,int b){
    real arctheta = theta * 2 * PI;
    int x = cos(arctheta) * len;
    int y = sin(arctheta) * len;
    p.setBrushColor(r,g,b);
    p.addLine(150,150,150+x,y+150);
}

def void drawClock(PaintPad p,Time t){
    for(int i = 1 ; i < 13;i++){
        real arctheta = ((real)i-3)/6 * PI;
        int x = cos(arctheta) * 140;
        int y = sin(arctheta) * 140;
        p.addString("" + i,150 + x,150+y);
    }

    drawHand(p,t.hour,((real)t.hour-3) / 12  + ((real)t.minute)/ 60 / 12  
                        + ((real)t.second)/ 60 /60 / 12 ,70,0,0,255);
    drawHand(p,t.minute,((real)t.minute-15) / 60 +((real)t.second)/ 60 /60,110,0,255,0);
    drawHand(p,t.second,((real)t.second-15) / 60,130,255,0,0);
}

def void drawClockReal(PaintPad p,real hour,real minute,real second){
    for(int i = 1 ; i < 13;i++){
        real arctheta = ((real)i-3)/6 * PI;
        int x = cos(arctheta) * 140;
        int y = sin(arctheta) * 140;
        p.addString("" + i,150 + x,150+y);
    }

    drawHand(p,hour,(hour-3) / 12  + (minute)/ 60 / 12  
                            + (second)/ 60 /60 / 12 ,70,0,0,255);
    drawHand(p,minute,(minute-15) / 60 +(second)/ 60 /60,110,0,255,0);
    drawHand(p,second,(second-15) / 60,130,255,0,0);
}

{

    PaintPad pad = new PaintPad("ClockInXScript",300,300);

    Thread t = new Thread(
                    new Runnable->{
                        MyTime t = new MyTime;
                        getTime(t);
                        real h = t.hour;
                        real m = t.minute;
                        real s = t.second;
                        while(true){
                            if(s > 59.5){
                                s = 0;
                                m += 1;
                                println(""+h+":"+m+":"+s);
                            } 
                            if(m > 59.5){
                                m = 0;
                                h += 1;
                            }
                            if(h > 23.5){
                                h = 0;
                            }
                            
                            pad.clear();
                            drawClockReal(pad,h,m,s);
                            pad.redraw();
                            sleep(20);
                            s += 0.02;
                        }
                    });
    println("Test UI clock");
    pad.show();
    t.start();
    pad.wait();
    t.interrupt();
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

struct shape {
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

def void drawRectangle(PaintPad p,int x,int y,int w,int h){
    p.addLine(x,y,x+w,y);
    p.addLine(x,y,x,y+h);
    p.addLine(x,y+h,x+w,y+h);
    p.addLine(x+w,y,x+w,y+h);
    
}
    int WIDTH = 50;
    int HEIGHT = 50;
    int TILE_WIDTH = 10;
    int CHESS_WIDTH = 8;
/*CHESS PLAYING*/
def void drawChessPad(PaintPad p,int x,int y)
{
    p.clear();
    p.setBrushColor(0,0,0);
    for(int i = 0;i <= WIDTH;i++){
        //println("x " + i + ":" + TILE_WIDTH*i+","+HEIGHT*TILE_WIDTH);
        p.addLine(TILE_WIDTH*i,0,TILE_WIDTH*i,HEIGHT*TILE_WIDTH);
    }
    for(int i = 0;i <= HEIGHT;i++){
        //println("y " + i + ":" + TILE_WIDTH*i+","+WIDTH*TILE_WIDTH);
        p.addLine(0,TILE_WIDTH*i,WIDTH*TILE_WIDTH,TILE_WIDTH*i);
    }
    p.setBrushColor(255,0,0);
    int co = (TILE_WIDTH - CHESS_WIDTH)/2;
    drawRectangle(p,x*TILE_WIDTH+co,y*TILE_WIDTH+co,CHESS_WIDTH,CHESS_WIDTH);

    p.redraw();
}


struct ChessPad:PaintPad{
    def this(string name,int w,int h){
        super(name,w,h);
    }
    
    def override void onClick(int id){
        
    }
}


/*CELL SIMULATION*/
def void drawWorld(PaintPad p,bool[][] worldmap)
{
    p.clear();
    p.setBrushColor(0,0,0);
    //println("Line " + _line_ + ":" + TILE_WIDTH);
    for(int i = 0;i <= WIDTH;i++){
        //println("x " + i + ":" + TILE_WIDTH*i+","+HEIGHT*TILE_WIDTH);
        p.addLine(TILE_WIDTH*i,0,TILE_WIDTH*i,HEIGHT*TILE_WIDTH);
    }
    for(int i = 0;i <= HEIGHT;i++){
        //println("y " + i + ":" + TILE_WIDTH*i+","+WIDTH*TILE_WIDTH);
        p.addLine(0,TILE_WIDTH*i,WIDTH*TILE_WIDTH,TILE_WIDTH*i);
    }
    p.setBrushColor(255,0,0);
    int co = (TILE_WIDTH - CHESS_WIDTH)/2;
    for(int x = 0;x < WIDTH;x++){
        for(int y = 0;y < HEIGHT;y++){
            if(worldmap[x][y])
                drawRectangle(p,x*TILE_WIDTH+co,y*TILE_WIDTH+co,CHESS_WIDTH,CHESS_WIDTH);
        }
    }
    p.redraw();
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

def void GameOfLife()
{
    //println("Line " + _line_ + ":" + TILE_WIDTH);
    const auto pad = new PaintPad("Game Of Life",WIDTH*TILE_WIDTH+1,HEIGHT*TILE_WIDTH+1);
    //println("Line " + _line_ + ":" + TILE_WIDTH);
    const bool[][] world = null ,world1 = new bool[][WIDTH],
             world2 = new bool[][WIDTH];
    for(int i = 0 ; i < WIDTH;i++){
        world1[i] = new bool[HEIGHT];
        world2[i] = new bool[HEIGHT];
    }

    srand(time());
    int sum = rand()%(WIDTH*HEIGHT);
    
    for(int i = 0;i < sum;i++){
        int x,y;
        do{
            x = rand()%WIDTH;
            y = rand()%HEIGHT;
        }while(world1[x][y]);
        world1[x][y] = true;
    }
    drawWorld(pad,world1);
    pad.show();
    sleep(200);
    //println("Line " + _line_ + ":" + TILE_WIDTH);
    Thread t = new Thread(
                    new Runnable->{
                        auto w = world;
                        auto w1 = world1;
                        auto w2 = world2;
                        while(true){
                            calMap(w1,w2);
                            w = w2;
                            w2 = w1;
                            w1 = w;
                            drawWorld(pad,w);
                            sleep(500);
                        }
                    });
    pad.show();
    t.start();
    pad.wait();
    t.interrupt();
    pad.close();
}

{
    println("Test for Game of Life");
    GameOfLife();
}

{
    println("Test for parser");
    parser p = new parser;
    lexer l = new lexer;
    //print("Enter the function you want to draw:f(x)=");
    string s;
    s = readString();
    
    l.init(s);
    p.init(l);
    Var v = p.getVar();
    Expr e = p.expr();

    /*draw a red line*/
    int last_x = -10000;
    int last_y = -10000;
    auto pad = new PaintPad("Parser",600,800);
    pad.setBrushColor(255,0,0);
    for (real x = 300; x > -300; x -= 1) {
        v.setValue(x);
        int y = e.getValue();
        pad.addPoint(x + 300 ,-y + 240);
        if(last_x > -10000){
            pad.addLine(last_x,last_y,x+300,-y+240);
        }
        last_x = x+300;
        last_y = -y+240;
    }
    println("drawing function line of f(x)=" + s);
    pad.setBrushColor(0,0,0);
    pad.addLine(0,240,600,240);
    pad.addLine(300,0,300,480);
    pad.show();
    pad.wait();
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
        println("default Wrong!");
    case 'a':
        println("Correct!");
        break;
    default:
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
    auto pad = new PaintPad("animated",500,800);
    pad.setBrushColor(255,0,0);
    
    pad.show();
    Thread t = new Thread(new Runnable->{
            int x,y;
            for(real off = 0;;off += 0.01 ){
                for(real theta = 0; theta <= 3.14 * 2 + 0.01;theta += 0.01){
                    x = theta * 100 + (-314 + 300);
                    y =  - sin(theta + off) * 100 + 240;
                    pad.addPoint(x,y);
                }
                pad.redraw();
                sleep(56);
                pad.clear();
            }
        });

    t.start();
    pad.wait();
    t.interrupt();
}

{
    println("Test for basic painting");
    PaintPad pad = new PaintPad("basic",480,600);
    pad.setBrushColor(25,25,255);
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
                pad.addPoint(x,y);
                pad.addPoint(x,y_m);
                pad.addPoint(x_m,y);
                pad.addPoint(x_m,y_m);
            }
        }
    }
  
    /*draw a red heart*/
    
    pad.setBrushColor(255,0,0);
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
                    pad.addLine(min*100 + 300  - 100,-y*100 + 240 - 80,
                                max*100 + 300 - 100,-y*100 + 240 - 80);
                    min = 1.5;
                    max = -15;
                }
            }
        }
        if(min <= max){
            pad.addLine(min*100 + 300  - 100,-y*100 + 240 - 80,
                        max*100 + 300  - 100,-y*100 + 240 - 80);
        }
    }
    
    /*draw a green round*/
    pad.setBrushColor(0,255,0);
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
                    pad.addLine(min + 300 + 100 ,-y + 240 + 100,
                                max + 300 + 100,-y + 240+100);
                    min = r;
                    max = -r;
                }
            }
        }
        if(min <= max){
            pad.addLine(min + 300 + 100 ,-y + 240 + 100,max + 300 + 100,-y + 240 + 100);
        }
    }
    
    
    pad.setBrushColor(0,0,0);
    pad.addLine(0,240,600,240);
    pad.addLine(300,0,300,480);
    pad.show();

    pad.wait();
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
    File f = new File();
    f.open("t.txt",false);
    println("write to file " + "t.txt");
    
    for (char y = 'a'; y <= 'z'; y++) {
        f.writech(y);
        int i = y;
        string x = ((string)i);
        f.writech(':');
        for(int i = 0 ; i < strlen(x);i++)
            f.writech(x[i]);
        if( y != 'z'){
            f.writech('\r');
            f.writech('\n');
        }
    }
    f.close();
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
    return strlen(str) <= 1?str
    :quicksort(lsstring(str,str[0],1)) + str[0] + quicksort(gestring(str,str[0],1));
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