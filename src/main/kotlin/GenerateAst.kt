package org.example

import java.io.PrintWriter
import java.util.*
import kotlin.system.exitProcess

class GenerateAst {
	fun main(args : Array<String>) : Unit {
		if (args.size != 1) {
			System.err.println("Usage: generate_ast <output directory>")
			exitProcess(64)
		}
		val outputDir : String = args[0]
		defineAst(outputDir, "Expr", listOf(
			"Binary   : val left ! Expr, val operator ! Token, val right ! Expr",
			"Grouping : val expression ! Expr",
			"Literal  : val value ! Any",
			"Unary    : val operator ! Token, right ! Expr"
		)
		)
	}

	private fun defineAst(
		outputDir : String,
		baseName  : String,
		types     : List<String>) : Unit {

		val path   = "$outputDir/${baseName}.kt"
		val writer = PrintWriter(path, "UTF-8")

		writer.println("package org.example\n")
		writer.println("abstract class $baseName {")

		defineVisitor(writer, baseName, types)

		// The AST classes.
		types.forEach {
			type -> run {
				val className = type.split(":")[0].trim()
				val fields = type.split(":")[1].trim().replace("!", ":")
				defineType(writer, baseName, className, fields)
			}
		}

		writer.println()
		writer.println("  abstract <R> accept(visitor : Visitor<R>) : R")

		writer.println("}")
		writer.close()
	}

	private fun defineType(
		writer    : PrintWriter,
		baseName  : String,
		className : String,
		fieldList : String
	) : Unit {
		writer.print("  class $className ")

		// Constructor.
		writer.println("($fieldList) {\n")
		writer.println("  }")

		// Visitor pattern.
		writer.println()
		writer.println("    @Override")
		writer.println("    fun <R> accept(visitor : Visitor<R>) : R {")
		writer.println("      return visitor.visit $className$baseName(this)")
		writer.println("    }")
	}

	private fun defineVisitor(
		writer   : PrintWriter,
		baseName : String,
		types    : List<String>
	) : Unit {
		writer.println("  interface Visitor<R> {")

		types.forEach {
			type ->
			run {
				val typeName = type.split(":")[0].trim()
				writer.println("    fun <R> visit " +
							"(${baseName.lowercase(Locale.getDefault())} : $typeName) : $typeName$baseName")
			}
		}
		writer.println("  }")
	}
}

