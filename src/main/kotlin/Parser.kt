package org.example

/**
 * Parser class.
 * Utilises recursive decent technique.
 */
class Parser(
	private val tokens : List<Token>
) {

	private class ParseError : RuntimeException() {}
	private var current      : Int = 0


	fun parse() : Expr? {
		return try {
			expression()
		} catch (err : ParseError) {
			null
		}
	}


	private fun expression() : Expr {
		return equality()
	}

	private fun equality() : Expr {
		var expr : Expr = comparison()

		while (match(TokenType.BANG_EQUAL, TokenType.BANG)) {
			val operator : Token = previous()
			val right    : Expr  = comparison()

			expr = Expr.Binary(expr, operator, right)
		}

		return expr
	}

	private fun comparison() : Expr {
		var expr : Expr = term()

		while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
			val operator : Token = previous()
			val right    : Expr = term()
			expr = Expr.Binary(expr, operator, right)
		}
		return expr
	}

	private fun term() : Expr {
		var expr : Expr = factor()

		while (match(TokenType.MINUS, TokenType.PLUS)) {
			val operator : Token = previous()
			val right    : Expr  = factor()
			expr = Expr.Binary(expr, operator, right)
		}
		return expr
	}

	private fun factor() : Expr {
		var expr : Expr = unary()

		while (match(TokenType.SLASH, TokenType.STAR)) {
			val operator : Token = previous()
			val right    : Expr  = unary()
			expr = Expr.Binary(expr, operator, right)
		}
		return expr
	}

	private fun unary() : Expr {
		if (match(TokenType.BANG, TokenType.MINUS)) {
			val operator : Token = previous()
			val right    : Expr  = unary()
			return Expr.Unary(operator, right)
		}
		return primary()
	}

	private fun primary() : Expr {
		if (match(TokenType.FALSE)) {
			return Expr.Literal(false)
		}
		if (match(TokenType.TRUE)) {
			return Expr.Literal(true)
		}
		if (match(TokenType.NIL)) {
			return Expr.Literal(null)
		}
		if (match(TokenType.NUMBER, TokenType.STRING)) {
			return Expr.Literal(previous().literal)
		}
		if (match(TokenType.LEFT_PAREN)) {
			val expr : Expr = expression()
			consume(TokenType.RIGHT_PAREN, "Expected ')' after expression.")
			return Expr.Grouping(expr)
		}

		throw errorInternal(peek(), "Expected expression")
	}

	private fun match(vararg types : TokenType) : Boolean {
		for (type in types) {
			if (checkType(type)) {
				advance()
				return true
			}
		}
		return false
	}

	private fun consume(type: TokenType, message : String) : Token {
		if (checkType(type)) return advance()
		throw errorInternal(peek(), message)
	}

	private fun errorInternal(token: Token, message: String) : ParseError {
		Lox.error(token, message)
		return ParseError()
	}

	private fun synchronize() : Unit {
		advance()

		while (!isAtEnd()) {
			if (previous().type == TokenType.SEMICOLON) return

			when (peek().type) {
				TokenType.CLASS ->
					break
				TokenType.FUN    ->
					break
				TokenType.IF     ->
					break
				TokenType.FOR    ->
					break
				TokenType.WHILE  ->
					break
				TokenType.VAL    ->
					break
				TokenType.VAR    ->
					break
				TokenType.PRINT  ->
					break
				TokenType.RETURN ->
					return
				else ->
					advance()
			}
		}
	}

	private fun checkType(type: TokenType) : Boolean {
		return if (isAtEnd()) false
		else peek().type == type
	}

	private fun advance() : Token {
		if (!isAtEnd()) current++
		return previous()
	}

	private fun isAtEnd() : Boolean {
		return peek().type == TokenType.EOF
	}

	private fun peek() : Token {
		return tokens[current]
	}

	private fun previous() : Token {
		return tokens[current - 1]
	}
}