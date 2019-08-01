import "system.xs";
import "../container/content.xs";

native<extension.predefined>{
    
    "SimpleRunnable":struct Runnable{
        def default virtual void run();
    };

    "SimpleThread":struct Thread{
        def this(Runnable r);
        def bool start();
        def bool interrupt();
        def bigint getThreadId();
        def void join(int time);
        def bool equals(Thread t);
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
    
    def bool wait(){
        return this.imp.wait();
    }
}

struct Condition {
    def default virtual bool isTrue();
}

struct Schedule : Runnable{
    int interval;
    Condition cond;
    Runnable run; 
    
    def this(int interval, Condition cond,Runnable run){
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

struct OnceScheduce : Condition {
	bool x;
	def this() {
		this.x = true;
	}
	
	def override bool isTrue() {
		if (this.x) {
			this.x = false;
			return true;
		}
		return false;
	}
}

struct Timer2 {
    Thread thread;
    
    def this(Runnable run, int interval){
        this.thread = new Thread(new Schedule(interval, new RepeatSchedule, run));
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

import "../container/bilist.xs";
struct ThreadContent : Content {
	Thread thread;
	
	def this(Thread t) {
		this.thread = t;
	}
	
	def override string toString() {
		return "";
	}
}

struct ThreadWork : Content {
	def default virtual void run(int tid);
	
	def override string toString() {
		return "";
	}
}

struct ThreadGroup {
	List thread_list;
	Trigger trigger;
	AtomicInteger counter;
	ThreadWork work;
	MutexLock lock;

	def this(int size) {
		this.thread_list = new List();
		this.trigger = new Trigger();
		this.counter = new AtomicInteger();
		this.lock = new MutexLock();
		auto _trigger = this.trigger;
		auto _counter = this.counter;
		auto _this = this;
		
		for (int i = 0; i < size; i++) {
			auto thread = new Thread(
				new Runnable -> {
					while(true) {
						if(!_trigger.wait()) {
							break;
						}
						if (_this.work != null) {
							_this.work.run(i);
						}
						_counter.decrementAndGet();
					}
				}
			);
			thread.start();
			this.thread_list.add(new ThreadContent(thread));
		}
	}
	
	def void run(ThreadWork work) {
		this.lock.wait();
		this.work = work;
		this.counter.setAndGet(this.thread_list.size());
		this.trigger.triggerAll();
		while(this.counter.get() > 0){;}
		this.work = null;
		this.lock.release();
	}
	
	def void close() {
		this.lock.wait();
		this.thread_list.forEach(new Consumer$thread->{
			((ThreadContent)thread).thread.interrupt();
		});
		this.thread_list.clear();
		this.lock.release();
	}
}

if(_isMain_) {
	auto grp = new ThreadGroup(20);
	println("Running ThreadGroup");
	grp.run(new ThreadWork$tid->{
		println("tid is " + tid);
	});
	grp.close();
	println("End Running ThreadGroup");
	flush();
	return 0;
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
    List        list;
    int 		capacity;
	
    def this(int capacity){
        this.full = new AtomicInteger();
        this.empty = new AtomicInteger();
        this.lock = new MutexLock();
    
        this.full.set(capacity);
        this.empty.set(0);
        this.list = new List();
		this.capacity = capacity;
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
	
	def int capacity() {
		return this.capacity;
	}
	
	def void clear() {
		this.lock.wait();
		this.full.setAndGet(this.capacity);
		this.empty.setAndGet(0);
		this.lock.release();
	}
}

struct ThreadWorkWrap : ThreadWork {
	AtomicInteger latch;
	def this() {
		this.latch = new AtomicInteger();
		this.latch.set(1);
	}
}

struct ThreadPool {
	List thread_list;
	ConcurrentQueue job_queue;
	MutexLock lock;

	def this(int size) {
		this.thread_list = new List();
		this.lock = new MutexLock();
		this.job_queue = new ConcurrentQueue(1024);

		auto _this = this;
		
		for (int i = 0; i < size; i++) {
			auto thread = new Thread(
				new Runnable -> {
					while(true) {
						auto work = (ThreadWorkWrap)_this.job_queue.pop();
						if (work != null) {
							work.run(i);
						}
						work.latch.decrementAndGet();
					}
				}
			);
			thread.start();
			this.thread_list.add(new ThreadContent(thread));
		}
	}

	def void execute(ThreadWorkWrap work) {
		this.job_queue.put(work);
	}

	def void wait(ThreadWorkWrap work) {
		if (work != null) {
			while( work.latch.get() != 0);
		}
	}
	
	def void close() {
		this.lock.wait();
		this.thread_list.forEach(new Consumer$thread->{
			((ThreadContent)thread).thread.interrupt();
		});
		this.thread_list.clear();
		this.job_queue.clear();
		this.lock.release();
	}
}