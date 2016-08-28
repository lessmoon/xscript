package extension.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lessmoon on 2016/8/15.
 */


public abstract class PaintPadImp extends JFrame {
    private final List<Item> itemList = Collections.synchronizedList(new ArrayList<>());
    private final List<StringItem> stringItemList = Collections.synchronizedList(new ArrayList<>());
    Color brushColor = Color.BLACK;

    public PaintPadImp(String name, final int height, final int width) {
        super(name);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                PaintPadImp.this.onClose();
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                PaintPadImp.this.onClick(e);
            }
        });

        JPanel jp = new JPanel() {
            public void paint(Graphics g) {
                synchronized (this) {
                    super.paint(g);
                    synchronized (PaintPadImp.this.itemList) {
                        for (Item i : itemList) {
                            g.setColor(i.color);
                            g.drawLine(i.x1, i.y1, i.x2, i.y2);
                        }
                    }
                    synchronized (PaintPadImp.this.stringItemList) {
                        for (StringItem i : stringItemList) {
                            g.setColor(i.color);
                            g.drawString(i.str, i.x, i.y);
                        }
                    }

                }
            }
        };

        jp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                PaintPadImp.this.onMouseClick(e);
            }
        });
        jp.setPreferredSize(new Dimension(width, height));
        add(jp, BorderLayout.CENTER);
    }

    public abstract void onClose();


    public abstract void onClick(KeyEvent e);

    public abstract void onMouseClick(MouseEvent e);

    public Object getLockObject(){
        return this.getTreeLock();
    }

    public void open() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void close() {
        this.dispose();
    }

    public void clear() {
        this.itemList.clear();
        this.stringItemList.clear();
    }

    public int addLine(int x1, int y1, int x2, int y2) {
        int c;
        synchronized (itemList) {
            c = itemList.size();
            itemList.add(new Item(x1, y1, x2, y2, brushColor));
        }
        return c;
    }

    public int addString(String string, int x, int y) {
        int c;
        synchronized (stringItemList) {
            c = stringItemList.size();
            stringItemList.add(new StringItem(x, y, string, brushColor));
        }
        return c;
    }

    public int addPoint(int x, int y) {
        int c;
        synchronized (itemList) {
            c = itemList.size();
            itemList.add(new Item(x, y, x, y, brushColor));
        }
        return c;
    }

    public boolean setLine(int id, int x1, int y1, int x2, int y2) {
        if (id < 0) {
            return false;
        }

        synchronized (itemList) {
            int c = itemList.size();
            if (id < c){
                Item i = itemList.get(id);
                i.setX1(x1);
                i.setX2(x2);
                i.setY1(y1);
                i.setX2(y2);
                return true;
            }
            return false;
        }
    }

    public boolean setLineColor(int id){
        if (id < 0) {
            return false;
        }

        synchronized (itemList) {
            int c = itemList.size();
            if (id < c){
                itemList.get(id).setColor(brushColor);
                return true;
            }
            return false;
        }
    }

    public boolean setString(int id, String string){
        if( id < 0){
            return false;
        }
        synchronized (stringItemList){
            if(id < stringItemList.size()) {
                stringItemList.get(id).setStr(string);
                return true;
            }
            return false;
        }
    }

    public boolean setStringColor(int id){
       if( id < 0){
            return false;
        }
        synchronized (stringItemList){
            if(id < stringItemList.size()) {
                stringItemList.get(id).setColor(brushColor);
                return true;
            }
            return false;
        }
    }

    public boolean setStringPosition(int id, int x, int y){
       if( id < 0){
            return false;
        }
        synchronized (stringItemList){
            if(id < stringItemList.size()) {
                StringItem stringItem = stringItemList.get(id);
                stringItem.setX(x);
                stringItem.setY(y);
                return true;
            }
            return false;
        }
    }

    public boolean setPoint(int id, int x, int y){
        if(id < 0) {
            return false;
        }
        synchronized (itemList) {
            if(id < itemList.size()){
                Item i = itemList.get(id);
                i.setX1(x);
                i.setX2(x);
                i.setY1(y);
                i.setY2(y);
                return true;
            }
            return false;
        }
    }

    public boolean setPointColor(int id){
        if(id < 0) {
            return false;
        }
        synchronized (itemList) {
            if(id < itemList.size()){
                itemList.get(id).setColor(brushColor);
                return true;
            }
            return false;
        }
    }

    public void setBrushColor(int r, int g, int b) {
        this.brushColor = new Color(r, g, b);
    }

    public void clearStrings(){
        stringItemList.clear();
    }

    public void clearPointAndLine(){
        itemList.clear();
    }

    public void redraw(){
        this.repaint();
    }
}
