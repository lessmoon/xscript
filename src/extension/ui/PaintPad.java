package extension.ui;

import inter.expr.StructValue;
import inter.expr.Value;
import lexer.Token;
import symbols.Position;
import symbols.Struct;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Item {
    int x1;
    int y1;
    int x2;
    int y2;

    Color color;

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    void setY2(int y2) {
        this.y2 = y2;
    }

    public Color getColor() {
        return color;
    }

    void setColor(Color color) {
        this.color = color;
    }
    
    Item(int x1, int y1, int x2, int y2, Color c){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        color = c;
    }
    
    public void set(int x1,int y1,int x2,int y2,Color c){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        color = c;
    }
}

class CircleItem {
    int x;
    int y;
    int radius;
    Color color;


    public CircleItem(int x, int y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

class StringItem{
	int x,y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    String str;

	Color color;
	
	StringItem(int x, int y, String str, Color color){
		this.x = x;
		this.y = y;
		this.str = str;
		this.color = color;
	}
	
    public void set(int x,int y,String str,Color color){
        this.x = x;
		this.y = y;
		this.str = str;
		this.color = color;
    }
	
}

class Point{
    final int x;
    final int y;
    public Point(int x,int y){
        this.x = x;
        this.y = y;
    }
}

public class PaintPad extends JFrame{
    static PaintPad                     pp  ;
    static Color                        bc  = Color.BLACK;
    static final ArrayList<Item>        ilist = new ArrayList<Item>();
	static final ArrayList<StringItem>        slist = new ArrayList<StringItem>();

    static final BlockingQueue<Integer> KBQueue = new LinkedBlockingQueue<Integer>();
    static final BlockingQueue<Point> MCQueue = new LinkedBlockingQueue<Point>();
    
    public PaintPad(String name,int width,int height){
        super(name);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    KBQueue.put((int)' ');
                }catch(Exception err){
                    err.printStackTrace();
                }
            }
        });

        KBQueue.clear();
        addKeyListener(new KeyAdapter(){
            public void keyReleased(KeyEvent e) {
                try{
                    switch(e.getKeyCode()){
                    case KeyEvent.VK_LEFT:  case KeyEvent.VK_A:
                        KBQueue.put((int)'a');
                        break;
                    case  KeyEvent.VK_RIGHT:case KeyEvent.VK_D:
                        KBQueue.put((int)'d');
                        break;
                    case KeyEvent.VK_UP:    case KeyEvent.VK_W:
                        KBQueue.put((int)'w');
                        break;
                    case KeyEvent.VK_DOWN:  case KeyEvent.VK_S:
                        KBQueue.put((int)'s');
                        break;
                    case KeyEvent.VK_SPACE:case KeyEvent.VK_ESCAPE:
                        KBQueue.put((int)' ');
                        return;
                    default:
                        return;
                    }
                } catch(Exception errx  ){
                    return ;
                }
            }
        });
        
        JPanel jp = new JPanel(){
            public void paint(Graphics g){
                synchronized(this){
                    super.paint(g);
                    synchronized(PaintPad.this.ilist){
                        for(Item i : ilist){
                            g.setColor(i.color);
                            g.drawLine(i.x1,i.y1,i.x2,i.y2);
                        }
                    }
					synchronized(PaintPad.this.slist){
                        for(StringItem i : slist){
                            g.setColor(i.color);
                            g.drawString(i.str,i.x,i.y);
                        }
                    }
					
                }
            }
        };
        jp.addMouseListener(
            new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e) { 
                    try{
                        MCQueue.put(new Point(e.getX(),e.getY()));
                    }catch(Exception errx){
                        return;
                    }
                }
            }
        );
        jp.setPreferredSize(new Dimension(width,height));
        add(jp,BorderLayout.CENTER);
    }

    static int openPad(int w,int h,String name){
        if(pp != null){
            clearPad();
            closePad();
        }
        pp = new PaintPad(name,w,h);
        
        pp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pp.pack();
        //pp.setResizable(false);
        pp.setVisible(true);
        pp.setLocationRelativeTo(null);
        //System.out.println("pp " + pp.getHeight() + "," + pp.getWidth());
        return 1;
    }
    
    static int setBrushColor(int r,int g,int b){
        bc = new Color(r,g,b);
        return 1;
    }

    static int addPoint(int x,int y){
        int c = 0;
        synchronized(ilist){
            c = ilist.size();
            ilist.add(new Item(x,y,x,y,bc));
        }
        return c;
    }

    static int setPoint(int id,int x,int y){
        synchronized(ilist){
            if(ilist.size() <= id){
                return -1;
            }
            ilist.get(id).set(x,y,x,y,bc);
        }
        return id;
    }
    
    static int addLine(int x1,int y1,int x2,int y2){
        int c = 0;
        synchronized(ilist){
            c = ilist.size();
            ilist.add(new Item(x1,y1,x2,y2,bc));
        }
        return c;
    }
    
    static int setLine(int id,int x1,int y1,int x2,int y2){
        synchronized(ilist){
            if(ilist.size() <= id){
                return -1;
            }
            ilist.get(id).set(x1,y1,x2,y2,bc);
        }
        return id;
    }

	static int addString(String str,int x,int y){
		int c = 0;
        synchronized(slist){
            c = slist.size();
            slist.add(new StringItem(x,y,str,bc));
        }
		return c;
	}
	
    static int setString(int id,String str,int x,int y){
        synchronized(slist){
            if(slist.size() <= id){
                return -1;
            }
            slist.get(id).set(x,y,str,bc);
        }
		return id;
	}
    
	static int clearString(){
		synchronized(slist){
			slist.clear();
        }
		return 0;
	}
	
    static int paint(){
        pp.repaint();
        return 0;
    }
    
    static int drawPoint(int x,int y){
        int c = addPoint(x,y);
        pp.repaint();
        return c;
    }

    static int drawLine(int x1,int y1,int x2,int y2){
        int c = addLine(x1,y1,x2,y2);
        pp.repaint();
        return c;
    }
    
    static int clearPad(){
        synchronized(ilist){
            ilist.clear();
			slist.clear();
        }
        pp.repaint();
        return 1;
    }

    static int closePad(){
        //pp.setVisible(false);
        pp.dispose();
        pp = null;
        return 1;
    }

    static boolean KeyBoardEvent(StructValue el, Token fname){
        KBQueue.clear();
        Position pos = ((Struct)el.type).getVirtualFunctionPosition(fname);
        Value res = null;
        ArrayList<Value> p = new ArrayList<Value>();
        p.add(null);
        do{
            Integer c ;
            try {
                c = KBQueue.take();
            }catch(Exception e){
                continue;
            }
            p.set(0,new Value(c.intValue()));
            res = runtime.Interface.invokeVirtualFunctionOfStruct(el,pos,p);
        }while(res == Value.True);
        return true;
    }

    static boolean SimpleMouseEvent(StructValue el, Token fname){
        MCQueue.clear();
        Position pos = ((Struct)el.type).getVirtualFunctionPosition(fname);
        Value res = null;
        ArrayList<Value> p = new ArrayList<Value>();
        p.add(null);
        p.add(null);
        do{
            Point co ;
            try {
                co = MCQueue.take();
            }catch(Exception e){
                continue;
            }
            p.set(0,new Value(co.x));
            p.set(1,new Value(co.y));
            res = runtime.Interface.invokeVirtualFunctionOfStruct(el,pos,p);
        }while(res == Value.True);
        return true;
    }
    
    public static void main(String[] args) throws Exception {
        openPad(600,500,"Test");
        drawLine(100,100,400,450);
    }
}