package runtime;

import lexer.Token;
import symbols.Type;

public interface TypeTable {
    public Type getType(Token name);
}