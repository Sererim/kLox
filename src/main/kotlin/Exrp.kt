package org.example

abstract class Expr {

	interface Visitor<R> {
		fun visitBinaryExpr(expr   : Binary)    : R

		fun visitGroupingExpr(expr : Grouping)  : R

		fun visitUnaryExpr(expr    : Unary)     : R

		fun visitLiteralExpr(expr  : Literal)   : R

	}

	abstract fun <R> accept(visitor : Visitor<R>) : R

	class Binary(
		val left     : Expr,
		val operator : Token,
		val right    : Expr
	)   : Expr() {
		override fun <R> accept(visitor : Visitor<R>) : R {
			return visitor.visitBinaryExpr(this)
		}
	}

	class Grouping(
		val expression : Expr
	) : Expr() {
		override fun <R> accept(visitor: Visitor<R>): R {
			return visitor.visitGroupingExpr(this)
		}
	}

	class Unary(
		val operator : Token,
		val right    : Expr
	)    : Expr() {
		override fun <R> accept(visitor: Visitor<R>): R {
			return visitor.visitUnaryExpr(this)
		}
	}

	class Literal(
		val value: Any?
	)  : Expr() {
		override fun <R> accept(visitor: Visitor<R>): R {
			return visitor.visitLiteralExpr(this)
		}
	}

}