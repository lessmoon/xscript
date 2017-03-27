package inter.expr;

import lexer.Token;
import symbols.Array;
import symbols.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lessmoon on 2017/3/21.
 */
public class ArrayGen extends Expr {
    final Var stackVar;
    final Expr array;
    final Expr generator;
    final Expr condition;

    public ArrayGen(Token tok, Type p,
                    Var stackVar, Expr array, Expr generator, Expr condition) {
        super(tok, p);
        this.stackVar = stackVar;
        this.array = array;
        this.generator = generator;
        this.condition = condition;
    }

    @Override
    public boolean isChangeable() {
        return false;
    }

    @Override
    public Value getValue() {
        ArrayValue value = (ArrayValue) array.getValue();
        List<Value> list = new ArrayList<>();
        for (int i = 0; i < value.getSize(); i++) {
            Value element = value.getElement(i);
            stackVar.setValue(value);
            if (condition.getValue() == Value.True) {
                list.add(element);
            }
        }

        ArrayValue newArray = new ArrayValue((Array) type, list.size());
        int i = 0;
        for (Value v : list) {
            newArray.setElement(i++, v);
        }
        return newArray;
    }
}
