import "content.xs";

struct binode{
    binode next;
    binode prev;
    Content value;

    def this(Content value,binode prev,binode next){
        this.value = value;
        this.next = next;
        this.prev = prev;
    }

}

struct bilist{
    binode head;
    binode tail;

    def this(){
        this.head = new binode(null,null,null);
        this.tail = new binode(null,this.head,null);
        this.head.next = this.tail;
    }

    def void push_front(Content value){
        this.head.next = new binode(value,this.head,this.head.next);
        this.head.next.next.prev = this.head.next;
    }

    def void push_back(Content value){
        this.tail.prev = new binode(value,this.tail.prev,this.tail);
        this.tail.prev.prev.next = this.tail.prev;
    }

    def binode front(){
        return this.head.next;
    }
    
    def binode back(){
        return this.tail.prev;
    }

    def Content pop_front(){
        Content tmp = this.head.next.value;
        this.head.next = this.head.next.next;
        this.head.next.prev = this.head;
        return tmp;
    }

    def Content pop_back(){
        Content tmp = this.tail.prev.value;
        this.tail.prev = this.tail.prev.prev;
        this.tail.prev.next = this.tail;
        return tmp;
    }
}