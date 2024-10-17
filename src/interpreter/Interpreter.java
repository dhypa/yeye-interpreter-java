package interpreter;

import interpreter.Expr.Binary;
import interpreter.Expr.Grouping;
import interpreter.Expr.Literal;
import interpreter.Expr.Visitor;

public class Interpreter implements Visitor<Object> {

	public void interpret(Expr expression) {
		try {
			Object value = evaluate(expression);
			System.out.println(stringify(value));
		} catch (RunTimeError e) {
			runtimeError(e);
		}
	}
	public String stringify(Object value) {
		if (value==null) return "nil";
		if (value instanceof Double){
			String text = value.toString();
			if (text.endsWith(".0")) text = text.substring(0, text.length()-2);
			return text;
		}
		return value.toString();
	}
	public void runtimeError(RunTimeError e) {
		System.err.println(e.getMessage() + "\n[line "+e.token.line+"]");
		Main.hadRuntimeError = true;
	}

	public Object visitLiteralExpr(Literal expr) {
		return expr.value;
	}

	@Override
	public Object visitBinaryExpr(Binary expr) {
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);
		switch(expr.operator.type) {
			case MINUS:
				checkNumberOperands(expr.operator, left, right);
				return (double)left - (double)right;
			case STAR:
				checkNumberOperands(expr.operator, left, right);
				return (double)left * (double)right;
			case SLASH:
				checkNumberOperands(expr.operator, left, right);
				return (double)left / (double)right;
			case PLUS:
				if (left instanceof String || right instanceof String) {
					return left.toString()+right.toString();
				}
				if (left instanceof Double && right instanceof Double) {
					return (double)left+(double)right;
				}
				break;

			case GREATER:
				checkNumberOperands(expr.operator, left, right);
				return (double)left > (double)right;
			case GREATER_EQUAL:
				checkNumberOperands(expr.operator, left, right);
				return (double)left >= (double)right;
			case LESS:
				checkNumberOperands(expr.operator, left, right);
				return (double)left < (double)right;
			case LESS_EQUAL:
				checkNumberOperands(expr.operator, left, right);
				return (double)left <= (double)right;
			case SEMICOLON_EQUAL:
				checkNumberOperands(expr.operator, right);
				return !isEquals(left,right);
			case EQUAL_EQUAL:
				checkNumberOperands(expr.operator, right);
				return isEquals(left,right);
		}
		return null;
	}

	public boolean isEquals(Object a, Object b) {
		if (a==null && b==null) return true;
		if (a==null) return false;
		return a.equals(b);
	}

	@Override
	public Object visitGroupingExpr(Grouping expr) {
		return evaluate(expr.expression);
	}


	@Override
	public Object visitUnaryExpr(Expr.Unary expr) {
		Object right = evaluate(expr.right);

		switch (expr.operator.type) {
			case BANG:
				return !parseBool(right);
			case MINUS:
				checkNumberOperands(expr.operator, right);
				return -(double)right;
		}

		// Unreachable.
		return null;
	}

	private void checkNumberOperands(Token operator, Object operand) {
		if (operand instanceof Double) return;
		throw new RunTimeError(operator, "Operand must be a number.");
	}
	private void checkNumberOperands(Token operator,Object left, Object right) {
		if (left instanceof Double && right instanceof Double) return;
		throw new RunTimeError(operator, "Operands must be numbers.");
	}

	private boolean parseBool(Object object) {
		if (object == null) return false;
		if (object instanceof Boolean) return (boolean) object;
		return true;
	}

	private Object evaluate(Expr expr) {
		return expr.accept(this);
	}


}
