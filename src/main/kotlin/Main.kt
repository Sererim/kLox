package org.example

import org.example.Lox.runFile
import org.example.Lox.runPrompt
import kotlin.system.exitProcess

// Main entry point for the program.
fun entry(args : Array<String>): Unit {
	if (args.size > 1) {
		println("Usage: kLox [script]")
		exitProcess(64)
	} else if (args.size == 1) {
		runFile(args[0])
	} else {
		runPrompt()
	}
}

fun main(args : Array<String>) {
	runPrompt()
}