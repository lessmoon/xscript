package extension.ui;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.StructMethod;
import inter.expr.Constant;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;


/**
 * Created by lessmoon on 2017/2/24.
 */
public class MouseAdapterX extends Struct{

   public static class MouseAdapterProxy {
       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseClicked(Constant id, Constant x,Constant y) {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mousePressed(Constant id, Constant x,Constant y) {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseReleased(Constant id, Constant x,Constant y) {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseEntered(Constant id, Constant x,Constant y)  {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseExited(Constant id, Constant x,Constant y) {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseWheelMoved(Constant id, Constant x,Constant y)  {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseDragged(Constant id, Constant x,Constant y) {}

       @StructMethod(args = {"int","int","int"},virtual = true)
       public void mouseMoved(Constant id, Constant x,Constant y)  {}
   }

    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(MouseAdapterProxy.class,dic,typeTable,sname,true);
    }
}
