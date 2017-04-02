import "content.xs";
import "sequence.xs";
import "../lib/utils.xs";

struct binode{
    binode next;
    binode prev;
    Content value;

    def this(Content value,binode prev,binode next){
        this.value = value;
        this.next = next;
        this.prev = prev;
    }

    @string
    def string toString(){
        return this.value;
    }
}

struct List:Sequence{
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

    def override void add(Content value){
        this.push_back(value);
    }
    
    def binode front(){
        return this.head.next;
    }

    def override bool isEmpty(){
        return this.head.next == this.tail;
    }
    
    def override int size(){
        int c = 0;
        for(auto i = this.head.next;i.next != null;i = i.next,c++);
        return c;
    }
    
    def override Iterator iterator(){
        return new Iterator(this.head){
            binode node;
            def this(binode inode){
                this.node = inode;
            }
            
            def override void next(){
                this.node = this.node.next;
            }
            
            def override Content getValue(){
                return this.node.value;
            }
            
            def override bool hasNext(){
                return this.node.next.next != null;
            }
        };
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
    
    @string
    def string toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(binode i = this.front();i.next != null;i = i.next){
            sb.append(" " + i);
        }
        sb.append(" ]");
    }
}

def Stream Stream.reverse(){
    List s = new List;
    Iterator i;
    while((i = this.next()) != null){
        s.push_front(i.getValue());
    }
    return s.stream();
}

import "collector.xs";

def Collector toList(){
    return new Collector(){
        def this(){
            super(new List());
        }
    };
}