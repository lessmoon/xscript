package vm.detail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import inter.expr.Value;
import vm.Pointer;
import vm.Type;

/**
 * DivisionOperator
 */
public class DivisionOperator extends ArithBinaryOperator {
    public DivisionOperator(ReadOnlyPointer left, ReadOnlyPointer right, WriteOnlyPointer target, Type type) {
        super(left, right, target, type);
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value left, Value right) {
        Value v;
        switch (type) {
        case INT:
            v = new Value(left.valueAs(Integer.class) / right.valueAs(Integer.class));
            break;
        case BIGINT:
            v = new Value(left.valueAs(BigInteger.class).divide(right.valueAs(BigInteger.class)));
            break;
        case REAL:
            v = new Value(left.valueAs(Float.class) / right.valueAs(Float.class));
            break;
        case BIGREAL:
            v = new Value(
                    left.valueAs(BigDecimal.class).divide(right.valueAs(BigDecimal.class), RoundingMode.HALF_EVEN));
            break;
        case CHAR:
            v = new Value(left.valueAs(Char.class) / right.valueAs(Char.class));
            break;
        default:
            assert false;
            break;
        }
        ;
        return v;
    }
}