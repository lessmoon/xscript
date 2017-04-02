import "sequence.xs";

struct Collector{
    Sequence seq;
    def this(Sequence s){
        this.seq = s;
    }

    def virtual Sequence collect(){
        return this.seq;
    }

    def virtual void feed(Content c){
        this.seq.add(c);
    }
}