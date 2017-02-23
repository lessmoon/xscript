package extension.ui;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.StructMethod;
import inter.expr.Constant;
import inter.expr.StackVar;
import inter.expr.StructConst;
import inter.stmt.InitialFunction;
import inter.stmt.Stmt;
import inter.util.Para;
import lexer.Token;
import lexer.Word;
import runtime.Dictionary;
import runtime.Interface;
import runtime.TypeTable;
import symbols.Position;
import symbols.Type;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lessmoon on 2016/8/16.
 */



public class PaintPadX extends Struct {
    static Token onClick ,onMouseMove,onClose;

    public static class PadProxy{
        PaintPadImp imp ;

        PadProxy(PaintPadImp paintPadImp){
            this.imp = paintPadImp;
        }

        static void init(final Constant arg0,Constant name, Constant width, Constant height, Position onClick,Position onMouseClick,Position onClose,Position onPress ){
            PadProxy padProxy = new PadProxy(new PaintPadImp(name.valueAs(String.class),width.valueAs(Integer.class),height.valueAs(Integer.class)) {

                @Override
                public void onClose() {
                    StructConst val0 = (StructConst)arg0.getValue();
                    Interface.invokeVirtualFunctionOfStruct(val0,onClose,new ArrayList<>());
                }

                @Override
                public void onPress(KeyEvent e) {
                    StructConst val0 = (StructConst)arg0.getValue();
                    List<Constant> args = new ArrayList<>();
                    args.add(new Constant(e.getKeyCode()));
                    Interface.invokeVirtualFunctionOfStruct(val0,onPress,args);
                }

                @Override
                public void onClick(KeyEvent e) {
                    StructConst val0 = (StructConst)arg0.getValue();
                    List<Constant> args = new ArrayList<>();
                    args.add(new Constant(e.getKeyCode()));
                    Interface.invokeVirtualFunctionOfStruct(val0,onClick,args);
                }

                @Override
                public void onMouseClick(MouseEvent e) {
                   StructConst val0 = (StructConst) arg0.getValue();
                    List<Constant> args =  new ArrayList<>();
                    args.add(new Constant(e.getButton()));
                    args.add(new Constant(e.getX()));
                    args.add(new Constant(e.getY()));
                    Interface.invokeVirtualFunctionOfStruct(val0,onMouseClick,args);
                }
            });
            ((StructConst)arg0.getValue()).setExtension(padProxy);
        }

        @StructMethod(virtual = true)
        public void onClose(){}

        @StructMethod(virtual = true,args = {"int","int","int"})
        public void onMouseClick(Constant button,Constant x,Constant y){}

        @StructMethod(virtual = true,args = {"int"})
        public void onClick(Constant code){}

        @StructMethod(virtual = true,args = {"int"})
        public void onPress(Constant code){}
        
        @StructMethod
        public void open(){
            imp.open();
        }

        @StructMethod
        public void close(){
            imp.close();
        }

        @StructMethod
        public void waitLock(){
            synchronized (imp.getLockObject()){;}
        }

        @StructMethod
        public void clear(){
            imp.clear();
        }

        @StructMethod(args = {"string","int","int"},ret="int")
        public Constant addString(Constant str,Constant x,Constant y){
            return new Constant(imp.addString(str.valueAs(String.class),x.valueAs(Integer.class),y.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int","int"},ret="int")
        public Constant addCircle(Constant x,Constant y,Constant radius){
            return new Constant(imp.addCircle(x.valueAs(Integer.class),y.valueAs(Integer.class),radius.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int","int"},ret="bool")
        public Constant setCircle(Constant id,Constant x,Constant y){
            return Constant.valueOf(imp.setCircle(id.valueAs(Integer.class),x.valueAs(Integer.class),y.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int"},ret="bool")
        public Constant setCircleColor(Constant id){
            return Constant.valueOf(imp.setCircleColor(id.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int"},ret="bool")
        public Constant setCircleRadius(Constant id,Constant radius){
            return Constant.valueOf(imp.setCircleRadio(id.valueAs(Integer.class),radius.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int","int"})
        public void setBrushColor(Constant r,Constant g,Constant b){
            imp.setBrushColor(r.valueAs(Integer.class),g.valueAs(Integer.class),b.valueAs(Integer.class));
        }

        @StructMethod(args = {"int","int","int","int"},ret = "int")
        public Constant addLine(Constant x1,Constant y1,Constant x2,Constant y2){
            return new Constant(imp.addLine(x1.valueAs(Integer.class),y1.valueAs(Integer.class),x2.valueAs(Integer.class),y2.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int"},ret = "int")
        public Constant addPoint(Constant x,Constant y){
            return new Constant(imp.addPoint(x.valueAs(Integer.class),y.valueAs(Integer.class)));
        }

        @StructMethod(args={"int","int","int","int","int"},ret="bool")
        public Constant setLine(Constant id,Constant x1,Constant y1,Constant x2,Constant y2){
            return Constant.valueOf(imp.setLine(id.valueAs(Integer.class),x1.valueAs(Integer.class),y1.valueAs(Integer.class)
                    ,x2.valueAs(Integer.class),y2.valueAs(Integer.class)));
        }
       @StructMethod(args={"int","string"},ret="bool")
        public Constant setString(Constant id,Constant str){
            return Constant.valueOf(imp.setString(id.valueAs(Integer.class),str.valueAs(String.class)));
        }

        @StructMethod(args={"int"},ret="bool")
        public Constant setLineColor(Constant id){
            return Constant.valueOf(imp.setLineColor(id.valueAs(Integer.class)));
        }

        @StructMethod(args={"int"},ret="bool")
        public Constant setStringColor(Constant id){
            return Constant.valueOf(imp.setStringColor(id.valueAs(Integer.class)));
        }

        @StructMethod(args={"int"},ret="bool")
        public Constant setPointColor(Constant id){
            return Constant.valueOf(imp.setPointColor(id.valueAs(Integer.class)));
        }


        @StructMethod(args = {"int","int","int"},ret="bool")
        public Constant setStringPosition(Constant id,Constant x,Constant y){
            return Constant.valueOf(imp.setStringPosition(id.valueAs(Integer.class),x.valueAs(Integer.class),y.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int","int"},ret="bool")
        public Constant setPoint(Constant id,Constant x,Constant y){
            return Constant.valueOf(imp.setPoint(id.valueAs(Integer.class),x.valueAs(Integer.class),y.valueAs(Integer.class)));
        }

        @StructMethod
        public void clearString(){
            imp.clearStrings();
        }

        @StructMethod
        public  void clearPointAndLine(){
            imp.clearPointAndLine();
        }

       @StructMethod
       public void redraw(){
           imp.redraw();
       }
    }

    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        symbols.Struct s = ExtensionStructHelper.buildStructFromClass(PadProxy.class,dic,typeTable,sname,false);
        final Position onClick = s.getVirtualFunctionPosition(dic.getOrReserve("onClick")),
                onMouseClick = s.getVirtualFunctionPosition(dic.getOrReserve("onMouseClick")),
                onPress = s.getVirtualFunctionPosition(dic.getOrReserve("onPress")),
                onClose = s.getVirtualFunctionPosition(dic.getOrReserve("onClose"));
        List<Para> paraList = new ArrayList<>();
        paraList.add(new Para(s,Word.This));
        paraList.add(new Para(Type.Str,dic.getOrReserve("name")));
        paraList.add(new Para(Type.Int,dic.getOrReserve("width")));
        paraList.add(new Para(Type.Int,dic.getOrReserve("height")));

        s.defineInitialFunction(new InitialFunction(Word.This,new Stmt(){
            final StackVar arg0 = new StackVar(Word.This,s,0,0);
            final StackVar arg1 = new StackVar(dic.getOrReserve("name"),Type.Str,0,1);
            final StackVar arg2 = new StackVar(dic.getOrReserve("width"), Type.Int,0,2);
            final StackVar arg3 = new StackVar(dic.getOrReserve("height"),Type.Int,0,3);

            @Override
            public void run(){
                PadProxy.init(arg0.getValue(),arg1.getValue(),arg2.getValue(),arg3.getValue(),onClick,onMouseClick,onClose,onPress);
            }
        },paraList,s));
        return s;
    }

}
