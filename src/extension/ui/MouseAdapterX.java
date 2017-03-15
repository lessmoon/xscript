package extension.ui;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.StructMethod;
import inter.expr.Value;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;


/**
 * Created by lessmoon on 2017/2/24.
 */
public class MouseAdapterX extends Struct{

   public static class MouseAdapterProxy {
       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseClicked(Value id, Value x, Value y) {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mousePressed(Value id, Value x, Value y) {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseReleased(Value id, Value x, Value y) {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseEntered(Value id, Value x, Value y)  {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseExited(Value id, Value x, Value y) {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseWheelMoved(Value id, Value x, Value y)  {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseDragged(Value id, Value x, Value y) {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseMoved(Value id, Value x, Value y)  {}
   }

    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(MouseAdapterProxy.class,dic,typeTable,sname,true);
    }
}
