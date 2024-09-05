package org.example


data class Token(
	final val type    : TokenType,
	final val lexeme  : String,
	final val literal : Any,
	final val line    : Int,
) {
	override fun toString() : String {
		return "$type $lexeme $literal"
	}
}