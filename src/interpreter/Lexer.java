package interpreter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Character.isLetter;

public class Lexer {
	private final String source;
	private ArrayList<Token> tokens = new ArrayList<>();
	private int start = 0;
	private int current = 0;
	private int line = 1;
	private String linetwo = "";
	private static final Map<String, TokenType> keywords;
	static {
		keywords = new HashMap<>();
		keywords.put("and",    TokenType.AND);
		keywords.put("class",  TokenType.CLASS);
		keywords.put("else",   TokenType.ELSE);
		keywords.put("false",  TokenType.FALSE);
		keywords.put("for",    TokenType.FOR);
		keywords.put("fun",    TokenType.FUN);
		keywords.put("if",     TokenType.IF);
		keywords.put("nil",    TokenType.NIL);
		keywords.put("print",  TokenType.PRINT);
		keywords.put("return", TokenType.RETURN);
		keywords.put("super",  TokenType.SUPER);
		keywords.put("this",   TokenType.THIS);
		keywords.put("true",   TokenType.TRUE);
		keywords.put("var",    TokenType.VAR);
		keywords.put("while",  TokenType.WHILE);
	}

	Lexer(String source) {
		this.source = source;
	}

	List<Token> scanTokens() {
		while (current < source.length()) {
			start = current;
			scanToken();
		}
		tokens.add(new Token(TokenType.EOF, "", null, line));
		return tokens;
	}

	private void scanToken() {
		char c = devour();
		switch (c) {
			case '(':
				addToken(TokenType.LEFT_PAREN);
				break;
			case ')':
				addToken(TokenType.RIGHT_PAREN);
				break;
			case '{':
				addToken(TokenType.LEFT_BRACE);
				break;
			case '}':
				addToken(TokenType.RIGHT_BRACE);
				break;
			case ',':
				addToken(TokenType.COMMA);
				break;
			case '.':
				addToken(TokenType.DOT);
				break;
			case '=':
				addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
				break;
			case '-':
				addToken(TokenType.MINUS);
				break;
			case '+':
				addToken(TokenType.PLUS);
				break;
			case '*':
				addToken(TokenType.STAR);
				break;
			case '<':
				addToken(match('=')? TokenType.LESS_EQUAL : TokenType.LESS);
				break;
			case '>':
				addToken(match('=')? TokenType.GREATER_EQUAL : TokenType.GREATER);
				break;
			case ';':
				addToken(match('=') ? TokenType.SEMICOLON_EQUAL : TokenType.SEMICOLON);
				break;
			case '!':
				addToken(TokenType.BANG);
				break;
			case '/':
				if (match('/')) {
					while (peek() != '\n' && !isAtEnd()) devour();
				} else if (match('*')){
					while (peek() != '*' && peekTwo() != '/') devour();
					devour(); 
					devour();
				}
				else {
					addToken(TokenType.SLASH);
				}
				break;
			case '&':
				if (match('&')){
					addToken(TokenType.AND);
				} else {
					Main.error(line, "OR operator is '&&' not '&'");
				}
				break;
			case '|':
				if (match('|')) {
					addToken(TokenType.OR);
				} else {
					Main.error(line, "OR operator is '||' not '|'");
				}
				break;
			case '"':
				string();
				break;

			case ' ':
			case '\r':
			case '\t':
				// Ignore whitespace.
				break;

			case '\n':
				line++;
				break;


			default:
				if (isDigit(c)) {
					number();
					break;
				} else if (isLetter(c)){
					identifier();
					break;
				}

				Main.error(line, "Unexpected character: \n [ " + c + " ]");
				break;
		}

	}

	private void identifier() {
		while (Character.isLetterOrDigit(peek())) devour();
		addToken(keywords.getOrDefault(source.substring(start, current), TokenType.IDENTIFIER));
	}

	private void number() {
		while (isDigit(peek()) && !isAtEnd()) devour();
		if (peek() == '.' && isDigit(peekNext())) {
			devour();
			while (isDigit(peek())) devour();
		}
		addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	public void string() {
		while (peek() != '"' && !isAtEnd()) {
			if (peek() == '\n') line++;
			devour();
		}
		if (isAtEnd()) Main.error(line, "Unterminated string");

		devour();
		String literal = source.substring(start + 1, current - 1);
		addToken(TokenType.STRING, literal);
	}


	private boolean match(char expected) {
		if (isAtEnd()) return false;
		if (source.charAt(current) != expected) return false;

		current++;
		return true;
	}

	private char peek() {
		if (isAtEnd()) return '\0';
		return source.charAt(current);
	}
	private char peekTwo() {
		if (isAtEnd()) return '\0';
		return source.charAt(current+1);
	}

	private char peekNext() {
		if (current + 1 >= source.length()) return '\0';
		return source.charAt(current + 1);
	}

	private char devour() {
		return source.charAt(current++);
	}

	// Adds a token with its literal to the arraylist
	private void addToken(TokenType type, Object literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}

	// Overloaded function exists to add a token with just the type, with no literal
	// Used where literal value is irrelevant, e.g. parentheses and operators
	private void addToken(TokenType type) {
		addToken(type, null);
	}

	private boolean isAtEnd() {
		return current >= source.length();
	}
}
