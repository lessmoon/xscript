package lexer;

import java.math.BigDecimal;

public class BigFloat extends Token {
    public final BigDecimal value;
    public BigFloat(BigDecimal v) {
        super(Tag.NUM);
        value = v;
    }
    public String toString() {
        return "" + value;
    }
}