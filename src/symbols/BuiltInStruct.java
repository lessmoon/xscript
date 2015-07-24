package symbols;

public class BuiltInStruct {
    public static const Struct t_thread = new Struct(new Word("thread")){
        static final int FUNC_RUN_GEN_ID = 0,FUNC_RUN_OFF_ID;
        
        static {
            this.addNormalFunction(new Word("start"),new FunctionBasic(){
                @Override
                void run(){
                    new Thread(){
                        void run(){
                            Struct.this.start();
                        }
                    }.start();
                }
            });
            //this.defineVirtualFunction(new Word("run")),new Function());
            FUNC_RUN_OFF_ID = this.getVirtualFunctionPosition(new Word("run")).index;
        }

        /*
         * start() will call the VirtualFunction(RUN)
         */
        void start(){
            this.getVirtualTable().getVirtualFunction(0,FUNC_RUN_OFF_ID).run();
        }
    }
}