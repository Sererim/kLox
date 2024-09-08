package org.example

/**
 * Token data class.
 * All tokens use it :)
 */
data class Token(
	val type    : TokenType,
	val lexeme  : String,
	val literal : Any,
	val line    : Int,
) {
	override fun toString() : String {
		return "$type $lexeme $literal"
	}
}