package vm.detail;

import java.math.BigDecimal;
import java.math.BigInteger;

import inter.expr.Value;
import vm.Pointer;
import vm.Type;

/**
 * AdditionOperator
 */
public class AdditionOperator extends ArithBinaryOperator {
    public AdditionOperator(ReadOnlyPointer left, ReadOnlyPointer right, WriteOnlyPointer target, Type type) {
        super(left, right, target, type);
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value left, Value right) {
        Value v;
        switch (type) {
        case INT:
            v = new Value(left.valueAs(Integer.class) + right.valueAs(Integer.class));
            break;
        case BIGINT:
            v = new Value(left.valueAs(BigInteger.class).add(right.valueAs(BigInteger.class)));
            break;
        case REAL:
            v = new Value(left.valueAs(Float.class) + right.valueAs(Float.class));
            break;
        case BIGREAL:
            v = new Value(left.valueAs(BigDecimal.class).add(right.valueAs(BigDecimal.class)));
            break;
        case CHAR:
            v = new Value(left.valueAs(Char.class) + right.valueAs(Char.class));
            break;
        case STRING://string concatenation
            v = new Value(left.valueAs(String.class) + right.valueAs(String.class));
            break;
        default:
            assert false;
            break;
        }
        ;
        return v;
    }
}