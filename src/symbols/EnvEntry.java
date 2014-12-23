package symbols;

import java.util.*;
import lexer.*;
import inter.*;

public class EnvEntry {
    public final Type  type;
    public final int   stacklevel;
    public final int   offset;
    EnvEntry(Type t,int sl,int o){
        type = t;
        stacklevel = sl;
        offset = o;
    }
}