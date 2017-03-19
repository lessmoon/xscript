import "sequence.xs";
import "collector.xs";

struct Consumer{
    def virtual Content apply(Content c);
}
struct Stream{
    def Stream filter(Consumer c);
    def void forEach(Consumer c);
    def Stream map(Consumer c);
    def Sequence reduce(Collector c);

    def virtual Iterator next();
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
            iter = new Iterator(this.mapper.apply(iter.getValue())){
                            Content value;
                            def this(Content value){
                                this.value = value;
                            }
                            
                            def override Content getValue(){
                                return this.value;
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
        action.apply(c.getValue());
    }
}

def Sequence Stream.reduce(Collector collector){
    Iterator iter;
    while((iter = this.next()) != null){
        collector.feed(iter.getValue());
    }
    return collector.collect();
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