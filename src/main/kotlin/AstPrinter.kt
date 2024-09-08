package org.example

/**
 * Object for pretty printing
 */
object AstPrinter : Expr.Visitor<String> {

	fun print(expr : Expr) : String {
		return expr.accept(this)
	}

	override fun visitBinaryExpr(expr: Expr.Binary): String {
		return parenthesize(
			expr.operator.lexeme,
			expr.left,
			expr.right
		)
	}

	override fun visitGroupingExpr(expr: Expr.Grouping): String {
		return parenthesize("'group'", expr.expression)
	}

	override fun visitLiteralExpr(expr: Expr.Literal): String {
		return if (expr.value.equals(null)) "nil"
					 else expr.value.toString()
	}

	override fun visitUnaryExpr(expr: Expr.Unary): String {
		return parenthesize(expr.operator.lexeme, expr.right)
	}

	private fun parenthesize(name : String, vararg exprs : Expr) : String {
		val builder : StringBuilder = StringBuilder()

		builder.append("( ").append(name)

		exprs.forEach { expr ->
			run {
				builder.append(" ").append(expr.accept(this))
			}
		}
		builder.append(" )")
		return builder.toString()
	}
}