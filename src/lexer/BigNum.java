package lexer;

import java.math.BigInteger;

public class BigNum extends Token {
    public final BigInteger value;
    public BigNum(BigInteger v) {
        super(Tag.BIGNUM);
        value = v;
    }
    
    @Override
    public String toString() {
        return "" + value;
    }
}