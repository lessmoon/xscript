package extension.predefined;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.Init;
import extension.annotation.StructMethod;
import inter.expr.Constant;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



/**
 * Created by lessmoon on 2016/7/29.
 */
public class MutexLock extends Struct {
    public static class MutexLockProxy {
        private Lock reentrantLock;

        @Init
        public void init(){
            reentrantLock= new ReentrantLock();
        }

        @StructMethod(ret="bool")
        public Constant tryLock(){
            return Constant.valueOf(reentrantLock.tryLock());
        }

        @StructMethod(value = "wait",ret="bool")
        public Constant _wait(){
            try {
                reentrantLock.lock();
            }catch (Exception e){
                return Constant.False;
            }
            return Constant.True;
        }

        @StructMethod(value="release",ret="bool")
        public Constant release(){
            try{
                reentrantLock.unlock();
            } catch (Exception e){
                return Constant.False;
            }
            return Constant.True;
        }
    }

    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(MutexLockProxy.class,dic,typeTable,sname,true);
    }
}
