/*standard list lib*/

struct list_node {
    list_node next;
    list_node prev;
    int  value;
}

struct list {
    list_node head;
    list_node tail;
    int count;
}

list_node _LIST_NULL_CONSTANT_;
_LIST_NULL_CONSTANT_ = _LIST_NULL_CONSTANT_.prev;

def list create_list(){
    list_node head;
    list_node tail;
    list l ;
    l.head = head;
    l.tail  = tail;
    l.count = 0;
    head.next = tail;
    head.prev = _LIST_NULL_CONSTANT_;
    tail.next  = _LIST_NULL_CONSTANT_;
    tail.prev  = head;
    return l;
}

def list push_back(list l,int value){
    list_node n;
    l.count++;
    n.value = value;
    n.prev = l.tail.prev;
    n.next = l.tail;
    n.prev.next = n;
    l.tail.prev = n;
    return l;
}

def bool list_isEmpty(list l){
    return l.count == 0;
}

def int list_size(list l){
    return l.count;
}

def int pop_front(list l){
    list_node n;
    if(l.count > 0){
        n = l.head.next;
        l.head.next = n.next;
        n.next.prev = n.prev;/*head*/
        l.count--;
        return n.value;
    }
    return -1;
}

def int pop_back(list l){
    list_node n;
    if(l.count > 0){
        n = l.tail.prev;
        l.tail.prev = n.prev;
        n.prev.next = n.next;/*tail*/
        l.count--;
        return n.value;
    }
    return -1;
}

def list union_list(list a,list b){
    a.tail.prev.next = b.head.next;
    b.head.next.prev = a.tail.prev;
    a.tail = b.tail;
    a.count += b.count;
    b = a;
    return a;
}

def list lesslist(list l,int v){
    list tmp = create_list();
    list_node i = l.head;
    while(i.next != l.tail){
        if(i.next.value < v)
            push_back(tmp,i.next.value);
        i = i.next;
    }
    return tmp;
}

def list greatlist(list l,int v){
    list tmp = create_list();
    list_node i = l.head;
    while(i.next != l.tail){
        if(i.next.value >= v)
            push_back(tmp,i.next.value);
        i = i.next;
    }
    return tmp;
}

def list qlsort(list l){
    if(l.count <= 1){
        return l;
    } else {
        int v = pop_front(l);
        list r = qlsort(lesslist(l,v));
        push_back(r,v);
        return union_list(r,qlsort(greatlist(l,v)));
    }
}

def string list_toString(list l){
    list_node i = l.head;
    string v = "[ ";
    while(i.next != l.tail){
        v += (string) i.next.value + " ";
        i = i.next;
    }
    v += "]";
    return v;
}