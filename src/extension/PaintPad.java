package extension;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

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

public class PaintPad extends JFrame{
    static PaintPad                 pp  ;
    static Color                    bc  = Color.BLACK;
    static final ArrayList<item>    ilist = new ArrayList<item>();

    public PaintPad(String name){
        super(name);
    }
    
    public void paint(Graphics g){
        super.paint(g);
        synchronized(ilist){
            for(item i : ilist){
                g.setColor(i.color);
                g.drawLine(i.x1,i.y1,i.x2,i.y2);
            }
        }
    }

    static int openPad(int w,int h){
        if(pp != null){
            clearPad();
            closePad();
        }
        pp = new PaintPad("Script");
        pp.setSize(w,h);
        pp.setLayout(null);
        pp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
        pp.setResizable(false);
        pp.setVisible(true);
        pp.setLocationRelativeTo(null);
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
        ilist.add(new item(x1,y1,x2,y2,bc));          
        return 1;
    }
    
    static int paint(){
        pp.repaint();
        return 0;
    }
    
    static int drawPoint(int x,int y){
        ilist.add(new item(x,y,x,y,bc));
        pp.repaint();
        return 1;
    }

    static int drawLine(int x1,int y1,int x2,int y2){
        ilist.add(new item(x1,y1,x2,y2,bc));
        pp.repaint();
        return 1;
    }
    
    static int clearPad(){
        ilist.clear();
        pp.repaint();
        return 1;
    }

    static int closePad(){
        //pp.setVisible(false);
        pp.dispose();
        pp = null;
        return 1;
    }

    public static void main(String[] args) throws Exception {
        openPad(600,500);
        drawLine(100,100,400,450);
    }
}