/*standard list lib*/

struct list_node {
    list_node next;
    int  value;
    
    def void set_next(list_node next){
        this.next = next;
    }
    
    def void set_value(int v){
        this.value = v;
    }
}

struct list {
    list_node head;
    list_node tail;
    int count;

    def void init(){
        int i = 22;
        list_node head,tail;
        this.head = head;
        this.head.set_next(tail);
        this.tail = tail;
        this.count = 0;
    }
    
    def void push_front(int v){
        list_node tmp;
        tmp.set_next(this.head.next);
        this.head.set_next(tmp);
        tmp.set_value(v);
        this.count ++;
    }
    
    def int pop_front(){
        if(this.count > 0){
            list_node tmp = this.head.next;
            this.head.set_next(tmp.next);
            this.count--;
            return tmp.value;
        }
        return -1;
    }
    
    def int size(){
        return this.count;
    }
}

def void print_list(list l){
    print("[");
    for(list_node n = l.head;n.next != l.tail;n = n.next){
        print(" " + n.next.value );
    }
    print(" ]\n");
}

def list union_list(list a,list b){
    list tmp;
    tmp.init();
    for(list_node n = a.head;n.next != a.tail;n = n.next){
        tmp.push_front(n.next.value);
    }
    for(list_node n = b.head;n.next != b.tail;n = n.next){
        tmp.push_front(n.next.value);
    }
    return tmp;
}

def list qlsort(list a){
    list tmp = a;
    if(a.size() > 0){
        int p = a.pop_front();
        list left,right;
        left.init();right.init();
        for(list_node n = a.head;n.next != a.tail;n = n.next){
            if(n.next.value <= p)
                left.push_front(n.next.value);
        }
        
        for(list_node n = a.head;n.next != a.tail;n = n.next){
            if(n.next.value > p)
                right.push_front(n.next.value);
        }
        left = qlsort(left);
        right = qlsort(right);
        right.push_front(p);
        tmp = union_list(left,right);
    }
    return tmp;
}