package runtime;

import lexer.Token;

public interface Dictionary {
    Token getOrReserve(String name);
}

