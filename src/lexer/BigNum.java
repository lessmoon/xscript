package lexer;

import java.math.BigInteger;

public class BigNum extends Token {
    public final BigInteger value;
    public BigNum(BigInteger v) {
        super(Tag.NUM);
        value = v;
    }
    public String toString() {
        return "" + value;
    }
}