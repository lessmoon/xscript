package runtime;

import lexer.Token;

public interface Dictionary {
    public Token getOrreserve(String name);
}

