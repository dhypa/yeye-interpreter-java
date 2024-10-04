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

		// Writing classes
		for (String type: types) {
			String classname = type.split(":")[0].trim();
			String fieldList = type.split(":")[1].trim();
			defineType(writer,baseName,classname,fieldList);

		}


		writer.println("}");
		writer.close();
	}

	private static void defineType(PrintWriter writer, String baseName, String classname, String fieldList) {
		String[] fields = fieldList.split(",");
		writer.println("    static class "+ classname + " extends "+baseName+"{");

		writer.println(classname + "("+fields+")"+"{");

		for(String field: fields) {
			field = field.trim();
			System.out.println("this."+field.split(" ")[1]);
		}
		
		//closes constructor
		writer.println("}");

		System.out.println("final");
		
		for(String field: fields) {
			field = field.trim();
			String[] splited = field.split(" ");
			System.out.println("final."+field.split(" ")[0]+" "+field.split(" ")[1]+";");
		}
		
		// closes base class
		writer.println("}");

	}
}





