package interpreter;


enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, BANG, SLASH, STAR,

    // One or two character tokens.
    SEMICOLON, SEMICOLON_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL, OR, AND,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    CLASS, ELSE, FALSE, FUN, FOR, IF, NIL,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    // End of File
    EOF
}

class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
