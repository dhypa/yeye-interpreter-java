package interpreter;

import interpreter.Expr.Binary;
import interpreter.Expr.Grouping;
import interpreter.Expr.Literal;
import interpreter.Expr.Unary;
import interpreter.Expr.Visitor;

public class Interpreter implements Visitor<Expr> {

	@Override
	public Expr visitBinaryExpr(Binary expr) {
		return null;
	}

	@Override
	public Expr visitGroupingExpr(Grouping expr) {
		return null;
	}

	@Override
	public Expr visitLiteralExpr(Literal expr) {
		return null;
	}

	@Override
	public Expr visitUnaryExpr(Unary expr) {
		return null;
	}
	
	
	
	
}
