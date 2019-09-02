package vm.detail;

import java.math.BigDecimal;
import java.math.BigInteger;

import inter.expr.Value;
import symbols.Struct;
import vm.ReadOnlyPointer;
import vm.Type;
import vm.VirtualMachine;
import vm.WriteOnlyPointer;

/**
 * Conversion: stateless!
 */
interface Conversion {
    public Value convert(VirtualMachine vm, Value value);
}

class NoConversion implements Conversion {
    @Override
    public Value convert(VirtualMachine vm, Value value) {
        return value;
    }
}

class IntConversion implements Conversion {
    final Type targetType;

    public IntConversion(Type targetType) {
        this.targetType = targetType;
    }

    @Override
    public Value convert(VirtualMachine vm, Value value) {
        int val = value.valueAs(Integer.class);
        switch (targetType) {
        case INT:
            return value;
        case REAL:
            return new Value((float) val);
        case BIGINT:
            return new Value(BigInteger.valueOf(val));
        case BIGREAL:
            return new Value(BigDecimal.valueOf(val));
        case CHAR:
            return new Value((char) val);
        case STRING:
            return new Value(String.valueOf(val));
        default:
            assert false;
            return null;
        }
    }
}

class RealConversion implements Conversion {
    final Type targetType;

    public RealConversion(Type targetType) {
        this.targetType = targetType;
    }

    @Override
    public Value convert(VirtualMachine vm, Value value) {
        float val = value.valueAs(Float.class);
        switch (targetType) {
        case INT:
            return new Value((int) val);
        case REAL:
            return value;
        case BIGINT:
            return new Value(BigInteger.valueOf((int) val));
        case BIGREAL:
            return new Value(BigDecimal.valueOf(val));
        case CHAR:
            return new Value((char) val);
        case STRING:
            return new Value(String.valueOf(val));
        default:
            assert false;
            return null;
        }
    }
}

class BigIntConversion implements Conversion {
    final Type targetType;

    public BigIntConversion(Type targetType) {
        this.targetType = targetType;
    }

    @Override
    public Value convert(VirtualMachine vm, Value value) {
        BigInteger val = value.valueAs(BigInteger.class);
        switch (targetType) {
        case INT:
            return new Value(val.intValue());
        case REAL:
            return new Value(val.floatValue());
        case BIGINT:
            return this;
        case BIGREAL:
            return new Value(new BigDecimal(val));
        case CHAR:
            return new Value((char) val.intValue());
        case STRING:
            return new Value(String.valueOf(val));
        default:
            assert false;
            return null;
        }
    }
}

class BigRealConversion implements Conversion {
    final Type targetType;

    public BigRealConversion(Type targetType) {
        this.targetType = targetType;
    }

    @Override
    public Value convert(VirtualMachine vm, Value value) {
        BigDecimal val = value.valueAs(BigDecimal.class);
        switch (targetType) {
        case INT:
            return new Value(val.intValue());
        case REAL:
            return new Value(val.floatValue());
        case BIGINT:
            return new Value(val.toBigInteger());
        case BIGREAL:
            return value;
        case CHAR:
            return new Value((char) val.intValue());
        case STRING:
            return new Value(String.valueOf(val));
        default:
            assert false;
            return null;
        }
    }
}

class CharConversion implements Conversion {
    final Type targetType;

    public CharConversion(Type targetType) {
        this.targetType = targetType;
    }

    @Override
    public Value convert(VirtualMachine vm, Value value) {
        char val = value.valueAs(Character.class);
        switch (targetType) {
        case INT:
            return new Value((int) val);
        case REAL:
            return new Value((float) val);
        case BIGINT:
            return new Value(BigInteger.valueOf(val));
        case BIGREAL:
            return new Value(new BigDecimal(val));
        case CHAR:
            return value;
        case STRING:
            return new Value(String.valueOf(val));
        default:
            assert false;
            return null;
        }
    }
}

class DownCastConversion implements Conversion {
    final symbols.Struct targetStruct;

    public DownCastConversion(symbols.Struct targetStruct) {
        this.targetStruct = targetStruct;
    }

    @Override
    public Value convert(VirtualMachine vm, Value value) {
        if (value == Value.Null) {
            return Value.Null;
        }
        if (value.type.equals(targetStruct) || ((symbols.Struct) value.type).isChildOf(targetStruct)) {
            return value;
        } else {
            //TODO: how to handle such exceptions?
            vm.raiseException("Downcasting error");
            return null;
        }
    }
}

class ConversionFactory {

}

/**
 * ConversionOperator
 */
public class ConversionOperator extends OutputUnaryOperator {
    private final Type sourceType;
    private final Type targetType;

    public ConversionOperator(ReadOnlyPointer input, WriteOnlyPointer target, Type sourceType, Type targetType) {
        super(input, target);
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    public ConversionOperator(ReadOnlyPointer input, WriteOnlyPointer target, Struct targetType) {
        this(input, target, Type.STRUCT, Type.STRUCT);
    }

    @Override
    protected Value calculate(VirtualMachine vm, Value value) {
    }

}