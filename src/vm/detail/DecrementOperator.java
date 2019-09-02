package vm.detail;

import java.math.BigDecimal;
import java.math.BigInteger;

import inter.expr.Value;
import lexer.BigNum;
import lexer.Char;
import lexer.Num;
import vm.ReadOnlyPointer;
import vm.Type;
import vm.VirtualMachine;
import vm.WriteOnlyPointer;

/**
 * DecrementOperator
 */
public class DecrementOperator extends OutputBinaryOperator {
    final Type type;

    public DecrementOperator(ReadOnlyPointer input, WriteOnlyPointer output, Type type) {
        super(input, output);
        this.type = type;
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value input) {
        switch (type) {
        case INT:
            return new Value(((Num) op).value - 1);
        case BIGINT:
            return new Value((((BigNum) op).value).subtract(BigInteger.ONE));
        case CHAR:
            return new Value(((Char) op).value - 1);
        default:// should not happen
            vm.raiseException("wrong type found");
            return null;
        }
    }
}