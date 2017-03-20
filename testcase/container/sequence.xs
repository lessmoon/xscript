import "content.xs";

struct Stream;
struct Consumer;

struct Sequence{
    def virtual Iterator iterator();
    def virtual Stream stream();
    def virtual void add(Content c);
    def virtual int size();
    def virtual bool isEmpty();
    def virtual void forEach(Consumer c);
}

import "stream.xs";