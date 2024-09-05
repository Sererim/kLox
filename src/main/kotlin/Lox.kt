package org.example

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess


object Lox {

	private var hadError : Boolean = false

	// Main entry point for the program.
	fun main(args : Array<String>): Unit {
		if (args.size > 1) {
			println("Usage: kLox [script]")
			exitProcess(64)
		} else if (args.size == 1) {
			runFile(args[0])
		} else {
			runPrompt()
		}
	}

	/**
	 * Function that evaluates the content of a file with Lox code.
	 * @param file a valid path to a file with Lox code.
	 */
	private fun runFile(file : String) : Unit {
		val bytes : ByteArray = Files.readAllBytes(Paths.get(file))
		evaluate(String(bytes, Charset.defaultCharset()))

		// Error
		if (hadError) exitProcess(64)

	}

	/**
	 * Main function for Lox REPL.
	 */
	private fun runPrompt() : Unit {
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

		tokens.forEach {
			token : Token -> {
				println(token)
			}
		}
	}

	fun error(line : Int, message : String) : Unit {
		report(line, "", message)
	}

	private fun report(line : Int, where : String, message : String) : Unit {
		System.err.println("[line + $line]\nError $where: $message")
		hadError = true
	}
}