import "system.xs";
import "../container/content.xs";

struct Runnable {
    def virtual void run();
}

native<extension.predefined>{

    "SimpleThread":struct Thread{
        def this(Runnable r);
        def bool start();
        def bool interrupt();
        def bigint getThreadId();
        def void join(int time);
    };

    "GetCurrentThread": Thread getCurrentThread();

    "MutexLock":struct MutexLock{
        def this();
        def bool tryLock();
        def bool wait();
        def bool release();
    };
    
    "Trigger":struct MyTrigger{
        def this();
        def void triggerAll();
        def bool wait();
    };
}

struct Trigger {
    MyTrigger imp;
    
    def this(){
        this.imp = new MyTrigger();
    }
    
    def void triggerAll(){
        this.imp.triggerAll();
    }
    
    def void wait(){
        this.imp.wait();
    }
}

struct Condition{
    def virtual bool isTrue();
}

struct Schedule : Runnable{
    int interval;
    Condition cond;
    Runnable run; 
    
    def this(int interval,Condition cond,Runnable run){
        this.interval = interval;
        this.cond     = cond;
        this.run      = run;
    }
    
    def override void run(){
        while(this.cond.isTrue()){
            sleep(this.interval);
            this.run.run();
        }
    }
}

struct RepeatSchedule : Condition{
    def override bool isTrue(){
        return true;
    }
}

struct Timer2 {
    Thread thread;
    
    def this(Runnable run,int interval){
        this.thread = new Thread(new Schedule(interval,new RepeatSchedule,run));
    }

    def void start(){
        this.thread.start();
    }
    
    def void stop(){
        this.thread.interrupt();
    }
}

struct Timer2Adapter : Runnable {
    Timer2 timer;
    
    def this(int interval){
        this.timer = new Timer2(this,interval);
    }
    
    def void start(){
        this.timer.start();
    }
    
    def void stop(){
        this.timer.stop();
    }
    
    def override void run(){}
}

struct AtomicInteger{
    int value;
    MutexLock lock;
    Trigger n;
    
    
    def this(){
        this.value = 0;
        this.lock = new MutexLock();
        this.n = new Trigger();
    }

    def int getAndSet(int value){
        this.lock.wait();
        auto res = this.value;
        this.value = value;
        this.n.triggerAll();
        this.lock.release();
        return res;
    }

    def int setAndGet(int value){
        this.getAndSet(value);
        return value;
    }

    def int incrementAndGet(){
        this.lock.wait();
        auto res = ++ this.value ;
        this.n.triggerAll();
        this.lock.release();
        return res;
    }

    def void waitAndDecrement(int min){
        do{
            this.lock.wait();
            if(this.value > min){
                -- this.value;
                this.n.triggerAll();
                this.lock.release();
                return;
            }
            this.lock.release();
            this.n.wait();
        }while(true);
    }
    
    def void waitAndIncrement(int max){
        do{
            this.lock.wait();
            if(this.value < max){
               ++ this.value;
               this.n.triggerAll();
            this.lock.release();
            return;
            }
            this.lock.release();
            this.n.wait();
        }while(true);
    }

    def int getAndIncrement(){
        return this.incrementAndGet() - 1;
    }

    def int decrementAndGet(){
        this.lock.wait();
        auto res = -- this.value ;
        this.n.triggerAll();
        this.lock.release();
        return res;
    }

    def int getAndDecrement(){
        return this.decrementAndGet() + 1;
    }

    @int
    def int get(){
        this.lock.wait();
        auto res = this.value;
        this.lock.release();
        return res;
    }

    def void set(int value){
        this.lock.wait();
        this.value = value;
        this.n.triggerAll();
        this.lock.release();
    }

    @string
    def string toString(){
        return this.get();
    }
}

struct Lock{
    def virtual void unlock();
    def virtual void lock();
}

struct ReadLock : Lock {
    Lock writeLock;
    
    def this(Lock write){
        this.writeLock = write;
    }

    def override void unlock(){}
    
    def override void lock(){}
}

struct AsyncRunnable : Runnable{
    Content value;
    
    def virtual Content get();
    
    def override void run(){
        this.value = this.get();
    }
}

struct Future {
    Thread t;
    AsyncRunnable r;
    
    def this(AsyncRunnable r){
        this.t = new Thread(r);
        this.r = r;
        this.t.start();
    }
    
    def Content get(){
        this.t.join(0);
        return this.r.value;
    }
}

import "../container/bilist.xs";

struct ConcurrentQueue {
    AtomicInteger full;
    AtomicInteger empty;
    MutexLock     lock;
    bilist        list;
    
    def this(){
        this.full = new AtomicInteger();
        this.empty = new AtomicInteger();
        this.lock = new MutexLock();
    
        this.full.set(100);
        this.empty.set(0);
        this.list = new bilist();
    }
    
    def void put(Content i){
        this.full.waitAndDecrement(0);
        this.lock.wait();   
        this.list.push_back(i);
        this.lock.release();
        this.empty.incrementAndGet();
    }
    
    def Content pop(){
        this.empty.waitAndDecrement(0);
        this.lock.wait();
        Content res = this.list.pop_front();
        this.lock.release();
        this.full.incrementAndGet();
        return res;
    }
    
    def int size(){
        return this.empty.get();
    }
}