package org.example


class Scanner (
	private val source : String,
	private val tokens : ArrayList<Token> = arrayListOf()
) {

	private var start   : Int = 0
	private var current : Int = 0
	private var line    : Int = 1

	private final val keywords : Map<String, TokenType> =
		hashMapOf<String, TokenType>().apply {
			put("fun", TokenType.FUN)

			put("and", TokenType.AND)
			put("or", TokenType.OR)

			put("print", TokenType.PRINT)

			put("class", TokenType.CLASS)
			put("super", TokenType.SUPER)
			put("this", TokenType.THIS)

			put("true", TokenType.TRUE)
			put("false", TokenType.FALSE)

			put("var", TokenType.VAR)
			put("val", TokenType.VAL)
			put("nil", TokenType.NIL)

			put("if", TokenType.IF)
			put("else", TokenType.ELSE)
			put("for", TokenType.FOR)
			put("while", TokenType.WHILE)

			put("return", TokenType.RETURN)
		}

	/**
	 * Main method of Scanner class.
	 * Scans entire source String for Tokens.
	 * @return tokens : ArrayList<Token>
	 */
	fun scanTokens() : ArrayList<Token> {
		while (!isAtEnd()) {
			start = current
			scanToken()
		}
		tokens.add(Token(type = TokenType.EOF, lexeme = "",
			literal = Unit, line = line))

		return tokens
	}

	/**
	 * Token scanner.
	 * Matches token to its TokenType
	 */
	private fun scanToken() {
		val c : Char = advance()
		when (c) {
			'(' -> addToken(TokenType.LEFT_PAREN)
			')' -> addToken(TokenType.RIGHT_PAREN)
			'{' -> addToken(TokenType.LEFT_BRACE)
			'}' -> addToken(TokenType.RIGHT_BRACE)
			',' -> addToken(TokenType.COMMA)
			'.' -> addToken(TokenType.DOT)
			';' -> addToken(TokenType.SEMICOLON)

			// Operators
			'+' -> addToken(TokenType.PLUS)
			'-' -> addToken(TokenType.MINUS)
			'*' -> addToken(TokenType.STAR)
			'!' -> addToken(
				if (match('=')) TokenType.BANG_EQUAL
				else TokenType.BANG
			)
			'=' -> addToken(
				if (match('=')) TokenType.EQUAL_EQUAL
				else TokenType.EQUAL
			)
			'<' -> addToken(
				if (match('=')) TokenType.LESS_EQUAL
				else TokenType.LESS
			)
			'>' -> addToken(
				if (match('=')) TokenType.GREATER_EQUAL
				else TokenType.GREATER
			)
			'/' -> {
				if (match('/')) {
					while ( peek() != '\n' && !isAtEnd() )
						advance()
				} else if (match('*')) {
					while (peek() != '*' && peekNext() != '/' && !isAtEnd() )
						advance()
					// To consume both * and / in the source.
					advance()
					advance()
				}else {
						addToken(TokenType.SLASH)
					}
			}

			// Special characters.
			' ', '\r', '\t' -> {}
			'\n' -> line++

			// String literal.
			'"' -> string()

			else ->
				if (isDigit(c)) {
					number()
				} else if (isAlpha(c)) {
					// is an Identifier
					identifier()
				} else {
					Lox.error(line, "Unexpected character ${advance()}")
				}
		}
	}

	/**
	 * Method to consume the identifier.
	 */
	private fun identifier() : Unit {
		while (isAlphaNumeric(peek())) advance()

		val text : String = source.substring(start, current)
		var type : TokenType? = keywords[text]

		if (type == null) type = TokenType.IDENTIFIER

		addToken(type)
	}

	/**
	 * Method that consumes entire number.
	 */
	private fun number() : Unit {
		while (isDigit(peek())) advance()

		// is a fraction ?
		if (peek() == '.' && isDigit(peekNext())) {

			// Eat it.
			advance()
			while (isDigit(peek())) advance()
		}

		addToken(TokenType.NUMBER, source.substring(start, current).toDouble())
	}

	/**
	 * Method that consumes entire string.
	 */
	private fun string() : Unit {
		while (peek() != '"' && !isAtEnd()) {
			if (peek() == '\n') line++
			advance()
		}

		if (isAtEnd()) {
			Lox.error(line, "Unterminated string.")
			return
		}

		advance()
		val value : String = source.substring(start + 1, current - 1)
		addToken(TokenType.STRING, value)
	}

	/**
	 * Method that peeks ahead for a matching character.
	 * @param expected Character to look for.
	 */
	private fun match(expected : Char) : Boolean {
		if (isAtEnd()) return false
		if (source[current] != expected) return false

		current++
		return true
	}

	/**
	 * Lookahead method.
	 */
	private fun peek() : Char {
		if (isAtEnd()) return '\n'
		return source[current]
	}

	private fun peekNext() : Char {
		if (current + 1 >= source.length) return '`'
		return source[current + 1]
	}

	private fun isAlpha(c : Char) : Boolean {
		return c in 'a'..'z' ||
					 c in 'A'..'Z' ||
					 c == '_'
	}

	private fun isAlphaNumeric(c : Char) : Boolean {
		return isAlpha(c) || isDigit(c)
	}

	private fun isDigit(c : Char) : Boolean {
		return c in '0'..'9'
	}

	private fun isAtEnd() : Boolean {
		return current >= source.length
	}

	private fun advance() : Char {
		current++
		return source[current - 1]
	}

	private fun addToken(type : TokenType) : Unit {
		addToken(type, Any())
	}

	private fun addToken(type : TokenType, literal : Any) : Unit {
		val text : String = source.substring(start, current)
		tokens.add(Token(type, text, literal, line))
	}
}