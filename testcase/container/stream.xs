import "sequence.xs";
import "collector.xs";

struct Consumer{
    @(
    def default virtual Content apply(Content c);
}

def void Iterator.forEachRemained(Consumer action){
    while(this.hasNext()){
        this.next();
        action(this.getValue());
    }
}

def void Sequence.forEach(Consumer c){
    auto i = this.iterator();
    while(i.hasNext()){
        i.next();
        c.apply(i.getValue());
    }
}

struct Comparator;

struct Stream{
    def Stream filter(Consumer c);
    def void forEach(Consumer c);
    def Stream map(Consumer c);
    def Sequence reduce(Collector c);
    def int count();
    def default virtual Iterator next();
    def Stream sort(Comparator c);
    def Stream skip(int c);
    def Stream reverse();
    def bool anyMatch(Consumer c);
    def bool allMatch(Consumer c);
}

struct TransformStream:Stream{
    Stream of;
    
    def this(Stream of){
        this.of = of;
    }
}

struct FilterStream:TransformStream{
    Consumer filter;
    
    def this(Stream of,Consumer filter){
        super(of);
        this.filter = filter;
    }

    def override Iterator next(){
        auto iter = this.of.next();
        while( iter != null && !(bool)this.filter.apply(iter.getValue()) ){
            iter = this.of.next();
        }

        return iter;
    }
    
}

struct MapStream:TransformStream{
    Consumer mapper;
    
    def this(Stream of,Consumer mapper){
        super(of);
        this.mapper = mapper;
    }

    def override Iterator next(){
        auto iter = this.of.next();
        if(iter != null){
            iter = new Iterator -> this.mapper.apply(iter.getValue());
        }
        return iter;
    }
}

def Stream Stream.filter(Consumer c){
    return new FilterStream(this,c);
}

def Stream Stream.map(Consumer c){
    return new MapStream(this,c);
}

def void Stream.forEach(Consumer action){
    Iterator c;
    while((c = this.next()) != null){
        action(c.getValue());
    }
}

def bool Stream.anyMatch(Consumer action){
    Iterator c;
    while((c = this.next()) != null){
        if((bool)(BoolContent)action(c.getValue())){
            return true;
        }
    }
    return false;
}

def bool Stream.allMatch(Consumer action){
    Iterator c;
    while((c = this.next()) != null){
        if(!(bool)(BoolContent)action(c.getValue())){
            return false;
        }
    }
    return true;
}

def int Stream.count(){
    int i = 0;
    Iterator c;
    while((c = this.next()) != null){
        i++;
    }
    return i;
}

def Sequence Stream.reduce(Collector collector){
    Iterator iter;
    while((iter = this.next()) != null){
        collector.feed(iter.getValue());
    }
    return collector.collect();
}

def Stream Stream.skip(int i){
    while(i-->0){
        this.next();
    }
    return this;
}

struct SequenceStream:Stream{
    Iterator iterator;

    def this(Sequence seq){
        this.iterator = seq.iterator();
    }

    def override Iterator next(){
        if(this.iterator.hasNext()){
            this.iterator.next();
            return this.iterator;
        }
        return null;
    }
}

def Stream Sequence.stream(){
    return new SequenceStream(this);
}

def Stream Iterator.stream(){
    return new Stream -> {
        if(this.hasNext()){
            this.next();
            return this;
        }
        return null;
    };
}

struct RangeStream:Stream{
    int i;
    int end;
    
    def this(int beg,int end){
        this.i = beg;
        this.end = end;
    }
    
    def override Iterator next(){
        if(this.i < this.end){
            this.i ++;
            return new Iterator -> new IntContent(this.i);
        }
        return null;
    }
}