import "content.xs";

struct HashPair{
    HashContent key;
    Content value;
    
    def this(HashContent key,Content value){
        this.key = key;
        this.value = value;
    }
    
    @string
    def string toString(){
        return "[" + this.key + ":" + this.value + "]";
    } 
}

struct HashMapNode{
    HashPair value;
    HashMapNode next;
    
    def this(HashPair value,HashMapNode next){
        this.next = next;
        this.value = value;
    }
    
    @string
    def string toString(){
        return "" + this.value + (this.next == null?"":("," + this.next));
    } 
}

struct HashMap{
    int size;
    int capcity;
    HashMapNode[] map;
    
    def this(){
        this.size = 0;
        this.capcity = 11;
        this.map = new HashMapNode[this.capcity];
    }

    def void rehash(int newcapcity){
        int oldcapcity = this.capcity;
        this.capcity = newcapcity;
        HashMapNode[] tmp = this.map;
        this.map = new HashMapNode[newcapcity];
        for(int i = 0 ; i < oldcapcity;i++){
            for(HashMapNode p = tmp[i]; p != null ; p = p.next ){
                int index = p.value.key.hash()%newcapcity;
                this.map[index] = new HashMapNode(p.value,this.map[index]);
            }
        }
    }

    def HashPair set(HashContent key,Content val){
        int h = key.hash();
        int index = h%this.capcity;
        HashMapNode p = this.map[index];
        if(p != null){
            if(p.value.key.equals(key)){
                this.map[index] = new HashMapNode(new HashPair(key,val),p.next);
                return p.value;
            }
            HashMapNode pn = p.next;
            while(pn != null){
                if(pn.value.key.equals(key)){
                    p.next = new HashMapNode(new HashPair(key,val),pn.next);
                    return pn.value;
                }
                p = p.next;
                pn = p.next;
            }
        }
        this.map[index] = new HashMapNode(new HashPair(key,val),this.map[index]);
        this.size++;
        if(this.size > 0.75 * this.capcity){
            this.rehash(2*this.capcity);
        }
        
        return null;
    }

    def int size(){
        return this.size;
    }
    
    def void clear(){
        this.size = 0;
    }
    
    @[
    def HashPair get(HashContent key){
        HashMapNode p = this.map[key.hash()%this.capcity];
        for(;p != null;p = p.next){
            if(p.value.key.equals(key)){
                return p.value;
            }
        }
        return null;
    }

    def HashPair remove(HashContent key){
        int h = key.hash();
        int index = h%this.capcity;
        HashMapNode p = this.map[index];
        if(p != null){
            if(p.value.key.equals(key)){
                this.size --;
                this.map[index] = p.next;
                if(this.size < 0.25 * this.capcity){
                    this.rehash(this.capcity/2);
                }
                return p.value;
            }
            HashMapNode pn = p.next;
            while(pn != null){
                if(pn.value.key.equals(key)){
                    this.size --;
                    p.next = pn.next;
                    if(this.size < 0.25 * this.capcity){
                        this.rehash(this.capcity/2);
                    }
                    return pn.value;
                }
                p = pn;
                pn = p.next;
            }
        }

        return null;
    }

    @string
    def string toString(){
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        for(int i = 0 ; i < this.capcity;i++){
            if(this.map[i] != null){
                buf.append(i).append(".").append(this.map[i]).append("\n");
            }
        }
        buf.append("}");
        return buf.toString();
    }
}


