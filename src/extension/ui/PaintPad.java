package extension.ui;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.concurrent.*;

import lexer.Token;
import inter.expr.Constant;
import inter.expr.StructConst;
import inter.stmt.FunctionBasic;
import symbols.Struct;
import symbols.Position;

class item {
    int x1;
    int y1;
    int x2;
    int y2;

    Color color;
    
    public item(int x1,int y1,int x2,int y2,Color c){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        color = c;
    }
}

class StringItem{
	final int x,y;
	final String str;

	final Color color;
	
	public StringItem(int x,int y,String str,Color color){
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
    static final ArrayList<item>        ilist = new ArrayList<item>();
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
                        for(item i : ilist){
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
        synchronized(ilist){
            ilist.add(new item(x,y,x,y,bc));
        }
        return 1;
    }

    static int addLine(int x1,int y1,int x2,int y2){
        synchronized(ilist){
            ilist.add(new item(x1,y1,x2,y2,bc));
        }
        return 1;
    }
    
	static int addString(String str,int x,int y){
		synchronized(slist){
            slist.add(new StringItem(x,y,str,bc));
        }
		return 0;
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
        synchronized(ilist){
            ilist.add(new item(x,y,x,y,bc));
        }
        pp.repaint();
        return 1;
    }

    static int drawLine(int x1,int y1,int x2,int y2){
        synchronized(ilist){
            ilist.add(new item(x1,y1,x2,y2,bc));
        }
        pp.repaint();
        return 1;
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

    static boolean KeyBoardEvent(StructConst el,Token fname){
        KBQueue.clear();
        Position pos = ((Struct)el.type).getVirtualFunctionPosition(fname);
        Constant res = null;
        ArrayList<Constant> p = new ArrayList<Constant>();
        p.add(null);
        do{
            Integer c ;
            try {
                c = KBQueue.take();
            }catch(Exception e){
                continue;
            }
            p.set(0,new Constant(c.intValue()));
            res = runtime.Interface.invokeVirualFunctionOfStruct(el,pos,p);
        }while(res == Constant.True);
        return true;
    }

    static boolean SimpleMouseEvent(StructConst el,Token fname){
        MCQueue.clear();
        Position pos = ((Struct)el.type).getVirtualFunctionPosition(fname);
        Constant res = null;
        ArrayList<Constant> p = new ArrayList<Constant>();
        p.add(null);
        p.add(null);
        do{
            Point co ;
            try {
                co = MCQueue.take();
            }catch(Exception e){
                continue;
            }
            p.set(0,new Constant(co.x));
            p.set(1,new Constant(co.y));
            res = runtime.Interface.invokeVirualFunctionOfStruct(el,pos,p);
        }while(res == Constant.True);
        return true;
    }
    
    public static void main(String[] args) throws Exception {
        openPad(600,500,"Test");
        drawLine(100,100,400,450);
    }
}