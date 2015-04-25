
/*
 * stack for xscript
 */

struct stack{
    int[] v;
    int num;
    
    def void init(int capacity);
    def void push(int i);
    def void pop();
    def int top();
    def int size();
    def bool is_empty();
}

def void stack.init(int capacity){
    this.v = new<int>(capacity);
    this.num = 0;
}

def void stack.push(int v){
    if(sizeof this.v == this.num){
        int[] tmp = this.v;
        this.v = new<int>(this.num * 2);
        for(int i = 0 ; i < this.num ; i++){
            this.v[i] = tmp[i];
        }
    }
    this.v[this.num] = v;
    this.num ++;
}

def void stack.pop(){
    if(this.num > 0){
        this.num--;
        if(this.num == (sizeof this.v)/4){
            int[] tmp = this.v;
            this.v = new<int>(this.num * 2);
            for(int i = 0 ; i < this.num ; i++){
                this.v[i] = tmp[i];
            }
        }
    }
}

def int stack.top(){
    return this.v[this.num-1];
}

def int stack.size(){
    return this.num;
}

def bool stack.is_empty(){
    return this.num == 0;
}