package org.example


fun main(args : Array<String>) {
	val expression : Expr = Expr.Binary(
		Expr.Unary(
			Token(TokenType.MINUS, "-", "", 1),
			Expr.Literal(123)
		),
		Token(TokenType.STAR, "*", "", 1),
		Expr.Grouping(Expr.Literal(45.67))
	)
	println(AstPrinter.print(expression))
}