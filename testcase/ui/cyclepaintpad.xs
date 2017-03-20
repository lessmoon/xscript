import "paintpad.xs";
import "../lib/system.xs";
import "../lib/concurrent.xs";

struct CyclePaintPad:Timer2Adapter{
    PaintPad pad;
    def this(PaintPad pad,int interval){
        super(interval);
        this.pad = pad;
    }
    
    def override void run(){
        this.pad.redraw();
    }
}