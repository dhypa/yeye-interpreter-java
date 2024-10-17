package interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
	private static final Interpreter interpreter = new Interpreter();
	private static boolean hadError = false;
	static boolean hadRuntimeError = false;

	public static void main(String[] args) throws IOException {

		if (args.length > 1) {
			System.out.println("Usage: yeye [FILE_PATH]");
			System.exit(69);
		} else if (args.length == 1) {
			runFile(args[0]);
		} else {
			REPL();
		}

	}

	private static void REPL() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Type \"quit\" to break");
		for (;;) {
			System.out.println(">> ");
			String line = in.readLine();
			if (line == null || line.equals("quit")){
				break;
			}
			run (line);
			hadError=false;

		}
	}

	private static void runFile(String arg) throws IOException {
		byte[] sourceCode = Files.readAllBytes(Path.of(arg));
		run(new String(sourceCode, Charset.defaultCharset()));
		if (hadError) System.exit(70);
	}

	private static void run(String sourceCode) {
		Lexer sc = new Lexer(sourceCode);
		List<Token> tokens = sc.scanTokens();

		Parser parser = new Parser(tokens);
		Expr expression = parser.parse();

		// Stop if there was a syntax error.
		if (hadError) System.exit(45);
		if (hadRuntimeError) System.exit(55);
	
		interpreter.interpret(expression);
		
		System.out.println("TREE:");
		System.out.println(new AstPrinter().print(expression));

	}

	static void error(int line, String message) {
		report(line, "", message);
	}

	private static void report(int line, String where,String message) {
		System.err.println("[line " + line + "] Error" + where + ": " + message);
		hadError = true;
	}
	static void error(Token token, String message) {
		if (token.type == TokenType.EOF) {
			report(token.line, " at end", message);
		} else {
			report(token.line, " at '" + token.lexeme + "'", message);
		}
	}
}