native<extension.system>{
    "sleep":void sleep(int duration);
}

native<extension.system>{
	"extime":struct Time{
		int hour;
		int minute;
		int second;
	};

	"GetTime":void getTime(Time d);

}

struct MyTime:Time{

	@string
	def string toString(){
		return "" + this.hour + ":" + this.minute + ":" + this.second;
	}
}

native<extension.system>{
    bool PutChar(char c);
    bool print(string str);
    bigint  time();
    int  getchar();
}

import "utils.xs";


def int readNumber(){
    StringBuffer l = new StringBuffer();
    char c;
    while(!isDigit(c = getchar()));

    do{
        l.appendCharacter(c);
    }while(isDigit(c = getchar()));
    return parseInt(l.toString());
}

def string readString(){
    StringBuffer l = new StringBuffer();
    char c;
    while(isBlank(c = getchar()));

    do{
        l.appendCharacter(c);
    }while('\n' != (c = getchar()));
    return l.toString();
}


def int repeat_print(string s,int c){
    int i = 0;
    for(i = 0;i < c;i++)
        print(s);
    return 0;
}

def void println(string s){
    print(s + "\n");
    return;
}

struct Timer{
    def void start();
    def void pause();
    def void resume();
    def int  getDuration();
    def void clear();

    int duration;
    int start;
}

def void Timer.start(){
    this.clear();
    this.start = time();
}

def void Timer.pause(){
    if(this.start != 0){
        this.duration += time() - this.start;
        this.start = 0;
    }
}

def void Timer.resume(){
    if(this.start == 0){
        this.start = time();
    }
}

def int Timer.getDuration(){
    if(this.start == 0){
        return this.duration;
    } else {
        return time() - this.start;
    }
}

def void Timer.clear(){
    this.start    = 0;
    this.duration = 0;
}

