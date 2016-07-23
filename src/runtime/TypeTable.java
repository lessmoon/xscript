package runtime;

import lexer.Token;
import symbols.Type;

public interface TypeTable {
   Type getType(Token name);
}