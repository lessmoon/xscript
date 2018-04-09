package extension.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by lessmoon on 2016/8/15.
 */
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

    Item(int x1, int y1, int x2, int y2, Color c) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        color = c;
    }

    public void set(int x1, int y1, int x2, int y2, Color c) {
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
    boolean fill;

    public CircleItem(int x, int y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.fill = false;
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
    
    public void setFill(boolean fill) {
        this.fill = fill;
    }
}

class StringItem {
    private int x;
    private int y;

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

    public void setFont(Font f){
        this.font = f;
    }

    public Font getFont(){
        return font;
    }

    private String str;

    private Color color;
    private Font font;

    StringItem(int x, int y, String str, Color color,Font f) {
        this.setX(x);
        this.setY(y);
        this.setStr(str);
        this.setColor(color);
        this.setFont(f);
    }

    public void set(int x, int y, String str, Color color,Font f) {
        this.setX(x);
        this.setY(y);
        this.setStr(str);
        this.setColor(color);
        this.setFont(f);
    }

}

public abstract class PaintPadImp extends JFrame {
    private final List<Item> itemList = Collections.synchronizedList(new ArrayList<>());
    private final List<Integer> freeItemList = Collections.synchronizedList(new LinkedList<>());
    private final List<StringItem> stringItemList = Collections.synchronizedList(new ArrayList<>());
    private final List<Integer> freeStringItemList = Collections.synchronizedList(new LinkedList<>());
    private final List<CircleItem> circleItemList = Collections.synchronizedList(new ArrayList<>());
    private final List<Integer> freeCircleItemList = Collections.synchronizedList(new LinkedList<>());
    
    private Color brushColor = Color.BLACK;
    private Font font = this.getFont();

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

            @Override
            public void keyPressed(KeyEvent e){
                super.keyReleased(e);
                PaintPadImp.this.onPress(e);
            }
        });


        JPanel jp = new JPanel() {
            public void paint(Graphics g) {
                synchronized (this) {
                    super.paint(g);
                    synchronized (PaintPadImp.this.itemList) {
                        for (Item i : itemList) {
                            if (i == null) {
                                continue;
                            }
                            g.setColor(i.color);
                            g.drawLine(i.x1, i.y1, i.x2, i.y2);
                        }
                    }
                    synchronized (PaintPadImp.this.stringItemList) {
                        for (StringItem i : stringItemList) {
                            if (i == null) {
                                continue;
                            }
                            g.setColor(i.getColor());
                            g.setFont(i.getFont());
                            g.drawString(i.getStr(), i.getX(), i.getY());
                        }
                    }

                    synchronized (PaintPadImp.this.circleItemList){
                        for(CircleItem i : circleItemList){
                            if (i == null) {
                                continue;
                            }
                            g.setColor(i.color);
                            g.drawOval(i.x,i.y,i.radius,i.radius);
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

    public abstract void onPress(KeyEvent e);

    public abstract void onClick(KeyEvent e);

    public abstract void onMouseClick(MouseEvent e);

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
            if (freeItemList.isEmpty()) {
                c = itemList.size();
                itemList.add(new Item(x1, y1, x2, y2, brushColor));
            } else {
                c = freeItemList.remove(0);
                itemList.set(c, new Item(x1, y1, x2, y2, brushColor));
            }
        }
        return c;
    }

    public boolean removeLine(int id) {
        if (id < 0) {
            return false;
        }

        synchronized (itemList) {
            int c = itemList.size();
            if (id < c && itemList.get(id) != null){
                itemList.set(id, null);
                freeItemList.add(id);
                return true;
            }
            return false;
        }
    }
    
    public int addCircle(int x,int y,int r){
        int c;
        synchronized (circleItemList) {
            if (freeCircleItemList.isEmpty()) {
                c = circleItemList.size();
                circleItemList.add(new CircleItem(x, y, r, brushColor));
            } else {
                c = freeCircleItemList.remove(0);
                circleItemList.set(c, new CircleItem(x, y, r, brushColor));
            }
        }
        return c;
    }
    
    public boolean removeCircle(int id) {
        if (id < 0) {
            return false;
        }
        synchronized (circleItemList) {
            int c = circleItemList.size();
            if (id < c && circleItemList.get(id) != null){
                circleItemList.set(id, null);
                freeCircleItemList.add(id);
                return true;
            }
            return false;
        }
    }
    
    public boolean setCircle(int id,int x,int y){
        if (id < 0) {
            return false;
        }

        synchronized (circleItemList) {
            int c = circleItemList.size();
            if (id < c && circleItemList.get(id) != null){
                circleItemList.get(id).setX(x);
                circleItemList.get(id).setY(y);
                return true;
            }
            return false;
        }
    }

    public boolean setCircleRadio(int id,int r){
        if (id < 0) {
            return false;
        }

        synchronized (circleItemList) {
            int c = circleItemList.size();
            if (id < c && circleItemList.get(id) != null){
                circleItemList.get(id).setRadius(r);
                return true;
            }
            return false;
        }
    }

    public boolean setCircleColor(int id){
        if (id < 0) {
            return false;
        }

        synchronized (circleItemList) {
            int c = circleItemList.size();
            if (id < c && circleItemList.get(id) != null){
                circleItemList.get(id).setColor(brushColor);
                return true;
            }
            return false;
        }
    }

    public int addString(String string, int x, int y) {
        int c;
        synchronized (stringItemList) {
            if (freeStringItemList.isEmpty()) {
                c = stringItemList.size();
                stringItemList.add(new StringItem(x, y, string, brushColor,font));
            } else {
                c = freeStringItemList.remove(0);
                stringItemList.set(c, new StringItem(x, y, string, brushColor,font));
            }
        }
        return c;
    }
    
    public boolean removeString(int id) {
        if( id < 0){
            return false;
        }
        synchronized (stringItemList){
            if(id < stringItemList.size() && stringItemList.get(id) != null) {
                stringItemList.set(id, null);
                freeStringItemList.add(id);
                return true;
            }
            return false;
        }
    }
    
    public int addPoint(int x, int y) {
        int c;
        synchronized (itemList) {
            if (freeItemList.isEmpty()) {
                c = itemList.size();
                itemList.add(new Item(x, y, x, y, brushColor));
            } else {
                c = freeItemList.remove(0);
                itemList.set(c, new Item(x, y, x, y, brushColor));
            }
        }
        return c;
    }
    
    public boolean removePoint(int id) {
        if(id < 0) {
            return false;
        }
        synchronized (itemList) {
            if(id < itemList.size() && itemList.get(id) != null){
                itemList.set(id, null);
                freeItemList.add(id);
                return true;
            }
            return false;
        }
    }
    
    public boolean setLine(int id, int x1, int y1, int x2, int y2) {
        if (id < 0) {
            return false;
        }

        synchronized (itemList) {
            int c = itemList.size();
            if (id < c && itemList.get(id) != null){
                Item i = itemList.get(id);
                i.setX1(x1);
                i.setX2(x2);
                i.setY1(y1);
                i.setY2(y2);
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
            if (id < c && itemList.get(id) != null){
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
            if(id < stringItemList.size() && stringItemList.get(id) != null) {
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
            if(id < stringItemList.size() && stringItemList.get(id) != null) {
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
            if(id < stringItemList.size() && stringItemList.get(id) != null) {
                StringItem stringItem = stringItemList.get(id);
                stringItem.setX(x);
                stringItem.setY(y);
                return true;
            }
            return false;
        }
    }

    public boolean setStringFont(int id){
        if( id < 0){
            return false;
        }
        synchronized (stringItemList){
            if(id < stringItemList.size() && stringItemList.get(id) != null) {
                StringItem stringItem = stringItemList.get(id);
                stringItem.setFont(font);
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
            if(id < itemList.size() && itemList.get(id) != null){
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
            if(id < itemList.size() && itemList.get(id) != null){
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

    public Font getMyFont() {
        return this.font;
    }

    public void setMyFont(Font font) {
        this.font = font;
    }
}
