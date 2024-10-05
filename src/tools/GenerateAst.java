package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Usage: generate_ast <output directory>");
			System.exit(64);
		}
		String outputDir = args[0];

		defineAst(outputDir, "Expr", Arrays.asList(
				"Binary   : Expr left, Token operator, Expr right",
				"Grouping : Expr expression",
				"Literal  : Object value",
				"Unary    : Token operator, Expr right"
				));
	}
	private static void defineAst(String outputDir, String baseName, List<String> types)throws IOException {
		String path = outputDir + "/" + baseName + ".java";
		PrintWriter writer = new PrintWriter(path, "UTF-8");

		//		writer.println("package com.craftinginterpreters.lox;");
		writer.println();
		writer.println("import java.util.List;");
		writer.println();
		writer.println("abstract class " + baseName + " {");
		
		defineVisitor(writer, baseName, types);

		// Writing classes
		for (String type: types) {
			String classname = type.split(":")[0].trim();
			String fieldList = type.split(":")[1].trim();
			defineType(writer,baseName,classname,fieldList);

		}
		
		writer.println("    abstract <R> R accept(Visitor<R> visitor);");


		writer.println("}");
		writer.close();
	}
	
	private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
		writer.println("    interface Visitor<R> {");
		for (String type: types) {
			String typeName = type.split(":")[0].trim();
			writer.println("    R visit"+typeName+ baseName+"("+typeName+" "+baseName.toLowerCase()+");");
		}
		writer.println("}");
		
	}

	private static void defineType(PrintWriter writer, String baseName, String classname, String fieldList) {
		String[] fields = fieldList.split(",");
		writer.println("    static class "+ classname + " extends "+baseName+"{"); // class definition

		// class constructor
		writer.println(classname + "("+fieldList+")"+"{");
		for(String field: fields) {
			String[] fieldSplitted = field.trim().split(" ");
			writer.println("this."+fieldSplitted[1]+"="+ fieldSplitted[1] +";");
		}
		writer.println("}");
		
		// implements visitor pattern
		writer.println();
		writer.println("@Override");
		writer.println("    <R> R accept(Visitor<R> visitor) {");
		writer.println("return visitor.visit"+classname+baseName+"(this);");
		writer.println("}");
		writer.println();
		
		
		// allocates fields
		for(String field: fields) {
			String[] splitted = field.trim().split(" ");
			writer.println("final "+splitted[0]+" "+splitted[1]+";");
		}
		writer.println("}");

	}
}





