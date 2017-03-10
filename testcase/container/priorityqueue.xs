struct Comparable{
    @<
    def virtual bool less(Comparable a);
    
    @>
    def bool more(Comparable a){
        return a.less(this);
    }
    
    @string
    def virtual string toString();
}



struct PriorityQueue{
    int size;
    int capacity;
    Comparable[] contents;

    def this(){
        this.size = 0;
        this.capacity = 15;
        this.contents = new Comparable[16];
    }
    
    def void swap(int i,int j){
        Comparable tmp = this.contents[i];
        this.contents[i]= this.contents[j];
        this.contents[j]= tmp;
    }
    
    def Comparable top(){
        return this.contents[1];
    }
    
    def Comparable pop(){
        if(this.size < 1){
            return null;
        }
        //swap the top to the end
        Comparable[] cc = this.contents;
        Comparable y = cc[1];
        
        this.swap(1,this.size);
        this.size--;
        int i = 1;
        int lc,rc;
        Comparable x = cc[1];
        do{
            lc = 2*i;
            rc = lc + 1;
            if(lc > this.size){
                break;
            } else if(rc > this.size){
                /*
                 *      [X]
                 *     /
                 *    Y (cc[lc]) 
                 *    we just need to compare X and Y
                 */
                if( x < cc[lc]){
                    this.swap(lc,i);
                }
                break;
            } else {
                /*
                 *      [X]
                 *     /  \
                 *    Y   Z
                 *    we need compare X,Y and Z,
                 *    choose the 'largest' one of them as the baseStruct node
                 */
                int max = lc;//the max index of child
                if(cc[lc] < cc[rc]){
                    max = rc;
                }
                if(cc[max] > x){
                    this.swap(max,i);
                    i = max;
                } else {
                    break;
                }
            }
        }while(true);
        
        if(this.size*4 < this.capacity){
            Comparable[] oldc = this.contents;
            this.capacity /= 2;
            this.contents = new Comparable[this.capacity + 1];
            for(int i = 1;i <= this.size;i++){
                this.contents[i] = oldc[i];
            }
        }
   
        return y;
    }
    
    def void push(Comparable e){
        if(++this.size > this.capacity){
            Comparable[] oldc = this.contents;
            this.capacity *= 2;
            this.contents = new Comparable[this.capacity + 1];
            for(int i = 1;i < this.size;i++){
                this.contents[i] = oldc[i];
            }
        }
        
        this.contents[this.size] = e;
        
        int x = this.size;
        //if the baseStruct node is less than the child node
        while(x >= 2 && this.contents[x/2] < e){
            this.swap(x,x/2);
            x /= 2;
        }
    }

    def void clear(){
        this.size = 0;
    }
    
    def int size(){
        return this.size;
    }
    
    @string
    def string toString(){
        string b = "[ ";
        for(int i = 1 ; i <= this.size;i++){
            b += "" + this.contents[i] + " " ;
        }
        return b+"]";
    }
}