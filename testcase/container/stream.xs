import "sequence.xs";
import "collector.xs";

struct Consumer{
    @(
    def default virtual Content apply(Content c);
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
    def virtual Iterator next();
    def Stream sort(Comparator c);
    def Stream skip(int c);
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
            auto value = this.mapper.apply(iter.getValue());
            iter = new Iterator(){
                            def override Content getValue(){
                                return value;
                            }
                    };
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