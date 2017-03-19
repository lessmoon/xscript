import "content.xs";

struct Stream;

struct Sequence{
    def virtual Iterator iterator();
    def virtual Stream stream();
    def virtual void add(Content c);
    def virtual int size();
    def virtual bool isEmpty();
}

import "stream.xs";