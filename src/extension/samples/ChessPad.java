package extension.ChessPad;

public class ChessPad {
    public static final int NONE = 0,BLACK = 1,WHITE = 2;
    public static final int WIDTH,HEIGHT;
    int[][] pad;
    int currentHand = WHITE;
    
    public ChessPad(int width,int height){
        WIDTH = width;
        HEIGHT =height;
        pad = new int[WIDTH][HEIGHT];
        clearPad();
    }

    
    public void clearPad(){
        for(int x = 0 ; x < WIDTH ; x++ ){
            for(int y = 0;y < HEIGHT; y++ ){
                pad[x][y] = NONE;
            }
        }
    }
    
    public void changeHand(){
        currentHand = currentHand == BLACK ? WHITE : BLACK;
        /*
         * DO SOMETHING
         */
    }
    
    /*
     * return  0 if nothing special happened
     *         1 if it is illegal
     *         2 if it is a win for black
     *         3 if it is a win for black
     */
    public int putChess(int x,int y){
        if(!checkLegal(x,y)){
            return 1;
        }

        int res = checkResult(x,y);
        pad[x][y] = currentHand;
        changeHand();
        return res;
    }
    
    /*
     * return true if it is ok to put chess here
     *     or false if this position is occuppied or it is out of the range.
     */
    public boolean checkLegal(int x,int y){
        if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT
            ){
            return false;
        }
        if(pad[x][y] != NONE){
            return false;
        }
        return true;
    }
    
    public int checkResult(int x,int y){
        int x0 = 0, x1 = 0,y0 = 0,y1 = 0;
        l = x - 4;
        if(l < 0){
            l = 0;
        }
        r = x + 4;
        if(r > WIDTH){
            r = WIDTH - 1;
        }
        
    }
    
    public int getChessByPosition(int x,int y){
        if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT
            ){
            return -1;
        }
        return pad[x][y];
    }
}