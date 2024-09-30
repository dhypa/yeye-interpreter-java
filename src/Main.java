
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
    private static boolean hadError = false;

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

        // For now, just print the tokens.
        for (Token token : tokens) {
            System.out.println(token);
        }

    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where,String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}