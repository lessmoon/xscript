package extension.ui;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.StructMethod;
import inter.expr.StackVar;
import inter.expr.StructValue;
import inter.expr.Value;
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

        static void init(final Value arg0, Value name, Value width, Value height, Position onClick, Position onMouseClick, Position onClose, Position onPress ){
            PadProxy padProxy = new PadProxy(new PaintPadImp(name.valueAs(String.class),width.valueAs(Integer.class),height.valueAs(Integer.class)) {

                @Override
                public void onClose() {
                    StructValue val0 = (StructValue)arg0.getValue();
                    Interface.invokeVirtualFunctionOfStruct(val0,onClose,new ArrayList<>());
                }

                @Override
                public void onPress(KeyEvent e) {
                    StructValue val0 = (StructValue)arg0.getValue();
                    List<Value> args = new ArrayList<>();
                    args.add(new Value(e.getKeyCode()));
                    Interface.invokeVirtualFunctionOfStruct(val0,onPress,args);
                }

                @Override
                public void onClick(KeyEvent e) {
                    StructValue val0 = (StructValue)arg0.getValue();
                    List<Value> args = new ArrayList<>();
                    args.add(new Value(e.getKeyCode()));
                    Interface.invokeVirtualFunctionOfStruct(val0,onClick,args);
                }

                @Override
                public void onMouseClick(MouseEvent e) {
                   StructValue val0 = (StructValue) arg0.getValue();
                    List<Value> args =  new ArrayList<>();
                    args.add(new Value(e.getButton()));
                    args.add(new Value(e.getX()));
                    args.add(new Value(e.getY()));
                    Interface.invokeVirtualFunctionOfStruct(val0,onMouseClick,args);
                }
            });
            ((StructValue)arg0.getValue()).setExtension(padProxy);
        }

        @StructMethod(virtual = true)
        public void onClose(){}

        @StructMethod(virtual = true,args = {"int","int","int"})
        public void onMouseClick(Value button, Value x, Value y){}

        @StructMethod(virtual = true,args = {"int"})
        public void onClick(Value code){}

        @StructMethod(virtual = true,args = {"int"})
        public void onPress(Value code){}
        
        @StructMethod(args = {"#.SimpleFont"})
        public void setFont(StructValue s){
            this.imp.setMyFont(((SimpleFont.SimpleFontProxy) s.getExtension()).getFont());
        }

        @StructMethod(ret = "#.SimpleFont")
        public StructValue getFont(){
            return new StructValue(symbols.Struct.StructPlaceHolder,new SimpleFont.SimpleFontProxy(this.imp.getMyFont()));
        }

        @StructMethod
        public void open(){
            imp.open();
        }

        @StructMethod
        public void close(){
            imp.close();
        }

        @StructMethod
        public void clear(){
            imp.clear();
        }

        @StructMethod(args = {"string","int","int"},ret="int")
        public Value addString(Value str, Value x, Value y){
            return new Value(imp.addString(str.valueAs(String.class),x.valueAs(Integer.class),y.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int","int"},ret="int")
        public Value addCircle(Value x, Value y, Value radius){
            return new Value(imp.addCircle(x.valueAs(Integer.class),y.valueAs(Integer.class),radius.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int","int"},ret="bool")
        public Value setCircle(Value id, Value x, Value y){
            return Value.valueOf(imp.setCircle(id.valueAs(Integer.class),x.valueAs(Integer.class),y.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int"},ret="bool")
        public Value setCircleColor(Value id){
            return Value.valueOf(imp.setCircleColor(id.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int"},ret="bool")
        public Value setCircleRadius(Value id, Value radius){
            return Value.valueOf(imp.setCircleRadio(id.valueAs(Integer.class),radius.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int","int"})
        public void setBrushColor(Value r, Value g, Value b){
            imp.setBrushColor(r.valueAs(Integer.class),g.valueAs(Integer.class),b.valueAs(Integer.class));
        }

        @StructMethod(args = {"int","int","int","int"},ret = "int")
        public Value addLine(Value x1, Value y1, Value x2, Value y2){
            return new Value(imp.addLine(x1.valueAs(Integer.class),y1.valueAs(Integer.class),x2.valueAs(Integer.class),y2.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int"},ret = "int")
        public Value addPoint(Value x, Value y){
            return new Value(imp.addPoint(x.valueAs(Integer.class),y.valueAs(Integer.class)));
        }

        @StructMethod(args={"int","int","int","int","int"},ret="bool")
        public Value setLine(Value id, Value x1, Value y1, Value x2, Value y2){
            return Value.valueOf(imp.setLine(id.valueAs(Integer.class),x1.valueAs(Integer.class),y1.valueAs(Integer.class)
                    ,x2.valueAs(Integer.class),y2.valueAs(Integer.class)));
        }
       @StructMethod(args={"int","string"},ret="bool")
        public Value setString(Value id, Value str){
            return Value.valueOf(imp.setString(id.valueAs(Integer.class),str.valueAs(String.class)));
        }

        @StructMethod(args={"int"},ret="bool")
        public Value setLineColor(Value id){
            return Value.valueOf(imp.setLineColor(id.valueAs(Integer.class)));
        }

        @StructMethod(args={"int"},ret="bool")
        public Value setStringColor(Value id){
            return Value.valueOf(imp.setStringColor(id.valueAs(Integer.class)));
        }

        @StructMethod(args={"int"},ret="bool")
        public Value setPointColor(Value id){
            return Value.valueOf(imp.setPointColor(id.valueAs(Integer.class)));
        }


        @StructMethod(args = {"int","int","int"},ret="bool")
        public Value setStringPosition(Value id, Value x, Value y){
            return Value.valueOf(imp.setStringPosition(id.valueAs(Integer.class),x.valueAs(Integer.class),y.valueAs(Integer.class)));
        }

        @StructMethod(args = {"int","int","int"},ret="bool")
        public Value setPoint(Value id, Value x, Value y){
            return Value.valueOf(imp.setPoint(id.valueAs(Integer.class),x.valueAs(Integer.class),y.valueAs(Integer.class)));
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
