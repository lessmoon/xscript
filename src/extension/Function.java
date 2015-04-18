package extension;

import inter.expr.Constant;
import java.util.ArrayList;

public abstract class Function {
    public abstract Constant run(ArrayList<Constant> paras);
}