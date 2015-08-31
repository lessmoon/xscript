package extension.samples;


class _3DVector {
    final int X,Y,Z;
    
    _3DVector(int x,int y,int z){
        this.X = x;
        this.Y = y;
        this.Z = z;
    }
    
    final _3DVector crossProduct(_3DVector v2){
        return new _3DVector(this.Y * v2.Z - this.Z * v2.Y,
                             this.Z * v2.X - this.X * v2.Z,
                             this.X * v2.Y - this.Y * v2.X);
    }
    
    final int innerProduct(_3DVector v2){
        return this.X*v2.X + this.Y*v2.Y + this.Z*v2.Z;
    }


    final boolean equals(_3DVector v){
        return this == v || (this.X == v.X&&this.Y == v.Y&&this.Z == v.Z);
    }
    
    final _3DVector getNegative(){
        return new _3DVector(-this.X,-this.Y,-this.Z);
    }
}

class Direction{
    static final int UP     = 0,DOWN = 1,
                     FRONT  = 2,BACK = 3,
                     LEFT   = 4,RIGHT= 5, ILLEGAL = -1;
    static final _3DVector  vUP = new _3DVector(0,0,1),vDOWN = new _3DVector(0,0,-1),
                            vFRONT = new _3DVector(1,0,0),vBACK = new _3DVector(-1,0,0),
                            vLEFT = new _3DVector(0,-1,0),vRIGHT = new _3DVector(0,1,0);
    static final int opposite(int dir){
        switch(dir){
        case UP:
            return DOWN;
        case DOWN:
            return UP;
        case FRONT:
            return BACK;
        case BACK:
            return FRONT;
        case LEFT:
            return RIGHT;
        case RIGHT:
            return LEFT;
        default:
            return ILLEGAL;
        }
    }

    static final _3DVector getVector(int dir){
        switch(dir){
        case UP:return vUP;
        case DOWN:return vDOWN;
        case LEFT:return vLEFT;
        case RIGHT:return vRIGHT;
        case FRONT:return vFRONT;
        case BACK:return vBACK;
        default:return null;
        }
    }

    static final int getDirection(_3DVector v){
        if(v.equals(vUP)){
            return UP;
        } else if(v.equals(vDOWN)){
            return DOWN;
        } else if(v.equals(vLEFT)){
            return LEFT;
        } else if(v.equals(vRIGHT)){
            return RIGHT;
        } else if(v.equals(vFRONT)){
            return FRONT;
        } else if(v.equals(vBACK)){
            return BACK;
        } else {
            return ILLEGAL;
        }
    }
    
    static final int up(int baseFront,int baseRight){
        _3DVector VFront = getVector(baseFront),VRight = getVector(baseRight);
        if(VFront.innerProduct(VRight) != 0){
            return ILLEGAL;
        }
        return getDirection(VFront.crossProduct(VRight));
    }
    
    static final int relativeDirection(int baseFront,int baseRight,int tar){
        if(tar == baseFront)
            return FRONT;
        else if(tar == baseRight)
            return RIGHT;
        else if(tar == opposite(baseFront))
            return BACK;
        else if(tar == opposite(baseRight))
            return LEFT;
        else if(tar == up(baseFront,baseRight))
            return UP;
        else if(tar == opposite(up(baseFront,baseRight)))
            return DOWN;
        else 
            return ILLEGAL;
    }
}

class State {
    private _3DVector front,right;
    
    State(){
        this.front = Direction.vFRONT;
        this.right = Direction.vRIGHT;
    }

    void change(int dir){
        _3DVector tmp;
        switch(dir){
        case Direction.UP:
            front = front.crossProduct(right);
            break;
        case Direction.DOWN:
            front = front.crossProduct(right).getNegative();
            break;
        case Direction.LEFT:
            tmp = front;
            front = right.getNegative();
            right = tmp;
            break;
        case Direction.RIGHT:
            tmp = front;
            front = right;
            right = tmp.getNegative();
            break;
        case Direction.BACK:
            front = front.getNegative();
        }
    }
    
    void rotate(boolean isClockwise){
        if(isClockwise){
            right = right.crossProduct(front);
        } else {
            right = front.crossProduct(right);
        }
    }
    
    int getFrontDirection(){
        return Direction.getDirection(front);
    }
    
    int getRightDirection(){
        return Direction.getDirection(right);
    }
    
    int getUpDirection(){
        return Direction.getDirection(front.crossProduct(right));
    }

    
    State getCopy(){
        State copy = new State();
        copy.front = this.front;
        copy.right = this.right;
        return copy;
    }
    
    /*
     * Which direction of the box is up,
     * At beginning,set UP for front = FRONT,LEFT,BACK,RIGHT,FRONT for UP and DOWN
     * After rotation we can adjust the set direction 
     */
    int getSetDirection(){
        int dir = Direction.UP;
        if(front.innerProduct(Direction.vUP) == 0){
            dir = Direction.DOWN;
        }
        int up = getUpDirection();
        if(dir == up){
            return Direction.UP;
        } else if(dir == Direction.opposite(up)){
            return Direction.DOWN;
        } else if(dir == Direction.getDirection(front.crossProduct(Direction.getVector(up)))){
            return Direction.RIGHT;
        } else {
            return Direction.LEFT;
        }
    }
}

class MatrixOfFace {
    static final int SIZE = 3;
    final int[][] colors = new int[SIZE][SIZE];
    int   up = Direction.UP;
    
    MatrixOfFace(int color){
        this.setColor(color);
    }
    
    void setColor(int color){
        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                colors[i][j] = color;
            }
        }
    }
    
    void rotate(boolean isClockwise){
        if(isClockwise){
            up = Direction.up(Direction.FRONT,up);
        } else {
            up = Direction.up(Direction.BACK,up);
        }
    }
    
    int at(int line,int raw,int dir){
        switch(Direction.relativeDirection(Direction.FRONT,up,dir)){
        case Direction.UP:
            return colors[line][raw];
        case Direction.DOWN:
            return colors[SIZE - 1 - line][SIZE - 1 - raw];
        case Direction.RIGHT:
            return colors[raw][SIZE - 1 - line];
        case Direction.LEFT:
            return colors[SIZE - 1 - raw][line]; 
        default:return Direction.ILLEGAL;
        }
    }
    
    void set(int line,int raw,int dir,int color){
        switch(Direction.relativeDirection(Direction.FRONT,up,dir)){
        case Direction.UP:
            colors[line][raw] = color;
        case Direction.DOWN:
            colors[SIZE - 1 - line][SIZE - 1 - raw] = color;
        case Direction.RIGHT:
            colors[raw][SIZE - 1 - line] = color;
        case Direction.LEFT:
            colors[SIZE - 1 - raw][line] = color; 
        }
    }
}

public class Cube {
    static final int SIZE = 6;
    State   state = new State();
    MatrixOfFace[] mats = new MatrixOfFace[SIZE];
    
    Cube(){
        for(int i = 0; i < SIZE;i++){
            mats[i] = new MatrixOfFace(i);
        }
    }
    
    void swap(int dir,int num){
        State next1 = state.getCopy();
        State next2 = state.getCopy();
        next2.change(dir);
        boolean isLine = (dir == Direction.RIGHT || dir == Direction.LEFT);
        for(int i = 0; i < MatrixOfFace.SIZE ;i++){
            exchange(next1.getFrontDirection(),next1.getSetDirection(),
                     next2.getFrontDirection(),next2.getSetDirection(),
                     isLine,num);
            next1 = next2;
            next2.change(dir);
        }
        
        if(num == MatrixOfFace.SIZE - 1 || num == 0){
            int rotationdir = Direction.FRONT;
            int changedir = Direction.FRONT;
            next2 = state.getCopy();
            if(num == 0){
                changedir = Direction.DOWN;
                if(dir == Direction.DOWN || dir == Direction.LEFT){
                    rotationdir = Direction.BACK;
                }
                if(dir == Direction.UP||dir == Direction.DOWN){
                    changedir = Direction.RIGHT;
                }
            } else {//if( num == MatrixOfFace.SIZE - 1){
                changedir = Direction.FRONT;
                
                if(dir == Direction.UP ||dir == Direction.RIGHT){
                    rotationdir = Direction.DOWN;
                }
                if(dir == Direction.UP ||dir == Direction.DOWN){
                    changedir = Direction.BACK;
                }
            }
            
            next2.change(changedir);
            //mats[next2.getFrontDirection()].rotate(rotationdir);
        }
    }
    
    void exchange(int f1,int dir1,
                  int f2,int dir2,
                  boolean isLine,int num){
        if(isLine){
            for(int i = 0;i < MatrixOfFace.SIZE;i++){
                int tmp = mats[f1].at(i,num,dir1);
                mats[f1].set(i,num,dir1,mats[f2].at(i,num,dir2));
                mats[f2].set(i,num,dir2,tmp);
            }
        } else {
            for(int i = 0; i< MatrixOfFace.SIZE;i++){
                int tmp = mats[f1].at(num,i,dir1);
                mats[f1].set(num,i,dir1,mats[f2].at(num,i,dir2));
                mats[f2].set(num,i,dir2,tmp);
            }
        }
    }
    
    void change(int dir){
        state.change(dir);
    }

    void rotateFace(boolean isClockwise){
        mats[state.getFrontDirection()].rotate(isClockwise);
    }
    
    void rotate(boolean isClockwise){
        state.rotate(isClockwise);
    }
    
    void show(){
        for(int i = 0;i < MatrixOfFace.SIZE;i++){
            for(int j = 0;j < MatrixOfFace.SIZE;j++){
                System.out.print(mats[state.getFrontDirection()].at(j,i,state.getSetDirection()));
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) throws Exception{
        Cube c = new Cube();
        while(true){
            c.show();
            int x = System.in.read();
            switch(x){
            case 'w':c.change(Direction.UP);break;
            case 's':c.change(Direction.DOWN);break;
            case 'a':c.change(Direction.LEFT);break;
            case 'd':c.change(Direction.RIGHT);break;
            case 'u':c.swap(Direction.UP,0);break;
            case 'o':c.swap(Direction.UP,2);break;
            case 'b':c.swap(Direction.DOWN,0);break;
            case 'm':c.swap(Direction.DOWN,2);break;
            case ' ':c.rotate(true);break;
            }
        }      
    }
}