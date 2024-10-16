package interpreter;

public class RunTimeError extends RuntimeException{
	
	final Token token;
	public RunTimeError(Token token, String message) {
		super(message);
		this.token = token;
		
		
	}
}
