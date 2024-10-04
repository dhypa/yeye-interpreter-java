public class Exp {
	
	static class Binary extends Exp{
		Binary(Exp left, Token operator, Exp right ){
			this.left=left;
			this.right=right;
			this.operator=operator;
		}
		final Exp left;
		final Exp right;
		final Token operator;
	}
	
}
