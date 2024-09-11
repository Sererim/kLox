package org.example

import com.sun.javafx.fxml.expression.Expression
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess


/**
 * Main Lox object.
 * Lox programs run through it.
 */
object Lox {

	private var hadError : Boolean = false


	/**
	 * Function that evaluates the content of a file with Lox code.
	 * @param file a valid path to a file with Lox code.
	 */
	fun runFile(file : String) : Unit {
		val bytes : ByteArray = Files.readAllBytes(Paths.get(file))
		evaluate(String(bytes, Charset.defaultCharset()))

		// Error
		if (hadError) exitProcess(64)

	}


	/**
	 * Main function for Lox REPL.
	 */
	fun runPrompt() : Unit {
		val input  = InputStreamReader(System.`in`)
		val reader = BufferedReader(input)
		var line : String = ""

		// REPL loop
		while (true) {
			print("> ")
			line = reader.readLine()
			if ( line.isEmpty() ) break
			evaluate(line)
			hadError = false
		}
	}

	/**
	 * Method for evaluation of code.
	 * @param source Lox source code.
	 */
	private fun evaluate(source: String) : Unit {
		val scanner = Scanner(source)
		val tokens : List<Token> = scanner.scanTokens()
		val parser = Parser(tokens)
		val expr : Expr? = parser.parse()

		if (hadError) return

		println(AstPrinter.print(expr!!))
	}

	fun error(line : Int, message : String) : Unit {
		report(line, "", message)
	}

	fun error(token: Token, message: String) : Unit {
		if (token.type == TokenType.EOF) {
			report(token.line, "at end", message)
		} else {
			report(token.line, " at '${token.lexeme}' ", message)
		}
	}

	private fun report(line : Int, where : String, message : String) : Unit {
		System.err.println("[line + $line]\nError $where: $message")
		hadError = true
	}
}