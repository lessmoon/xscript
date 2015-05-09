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


def list create_list(){
    list_node head = new<list_node>;
    list_node tail = new<list_node>;
    list l  = new<list> ;
    l.head = head;
    l.tail  = tail;
    l.count = 0;
    head.next = tail;
    head.prev = null;
    tail.next  = null;
    tail.prev  = head;
    return l;
}

def list push_back(list l,int value){
    list_node n  = new<list_node>;
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
    list_node n = new<list_node>;
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
    list_node n = new<list_node>;
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
    //println("GL = " + list_toString(tmp));
    return tmp;
}

def void reprint(char c,int v){
    while(v-- > 0){
        print(c);
    }
}



def list union_listc(list a,list b,int c){
    //reprint(' ',c*2);
    //print("|"+list_toString(a) + "U" + list_toString(b));
    a.tail.prev.next = b.head.next;
    b.head.next.prev = a.tail.prev;
    a.tail = b.tail;
    a.count += b.count;
    b = a;
    //reprint(' ',c*2);
    //println( "=" + list_toString(a));
    return a;
}

def list qlsort(list l,int level){
    reprint(' ',level*2);
    println("|s(" + list_toString(l) + ")");
    if(l.count <= 1){
        list x = create_list();
        return union_listc(x,l,level);
    } else {
        int v = pop_front(l);
        //println(v);
        list r = qlsort(lesslist(l,v),level+1);
        push_back(r,v);
        //println("r = " + list_toString(r));
        //println("line:=>>>>" + 155);
        return union_listc(r,qlsort(greatlist(l,v),level+1),level);
    }
}

