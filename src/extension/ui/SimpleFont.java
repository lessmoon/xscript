package extension.ui;

import extension.ExtensionStructHelper;
import extension.Struct;
import extension.annotation.Init;
import extension.annotation.StructMethod;
import inter.expr.Value;
import lexer.Token;
import runtime.Dictionary;
import runtime.TypeTable;

import java.awt.*;

/**
 * Created by lessmoon on 2017/3/12.
 */
public class SimpleFont extends Struct {

    @Override
    public symbols.Struct setup(Token sname, Dictionary dic, TypeTable typeTable) {
        return ExtensionStructHelper.buildStructFromClass(SimpleFontProxy.class,dic,typeTable,sname,false);
    }

    public static class SimpleFontProxy{
        private Font font;

        public SimpleFontProxy() {
        }

        public SimpleFontProxy(Font font) {
            this.font = font;
            if(this.font == null){
                this.font = new Font(null,0,0);
            }
        }

        @Init(param = {"string","int","int"})
        public void init(Value str, Value style, Value size){
            font = new Font(str.valueAs(String.class),style.valueAs(int.class),size.valueAs(int.class));
        }

        @StructMethod(ret = "string")
        public Value getFontName(){
            return new Value(font.getFontName());
        }

        public Font getFont() {
            return font;
        }

    }

}
