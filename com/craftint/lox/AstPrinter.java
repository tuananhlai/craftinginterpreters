// package com.craftint.lox;

// import com.craftint.lox.Expr.Binary;
// import com.craftint.lox.Expr.Grouping;
// import com.craftint.lox.Expr.Ternary;
// import com.craftint.lox.Expr.Variable;
// import com.craftint.lox.Expr.Visitor;

// class AstPrinter implements Expr.Visitor<String> {
// String print(Expr expr) {
// return expr.accept(this);
// }

// @Override
// public String visitBinaryExpr(Binary expr) {
// return parenthesize(expr.operator.lexeme, expr.left, expr.right);
// }

// @Override
// public String visitGroupingExpr(Expr.Grouping expr) {
// return parenthesize("group", expr.expression);
// }

// @Override
// public String visitLiteralExpr(Expr.Literal expr) {
// if (expr.value == null)
// return "nil";
// return expr.value.toString();
// }

// @Override
// public String visitUnaryExpr(Expr.Unary expr) {
// return parenthesize(expr.operator.lexeme, expr.right);
// }

// private String parenthesize(String name, Expr... exprs) {
// StringBuilder builder = new StringBuilder();

// builder.append("(").append(name);
// for (Expr expr : exprs) {
// builder.append(" ");
// builder.append(expr.accept(this));
// }
// builder.append(")");

// return builder.toString();
// }

// @Override
// public String visitTernaryExpr(Ternary expr) {
// return parenthesize(expr.operator.lexeme, expr.first, expr.second,
// expr.third);
// }

// public static void main(String[] args) {
// Expr expression = new Expr.Binary(
// new Expr.Unary(new Token(TokenType.MINUS, "-", null, 1), new
// Expr.Literal(123)),
// new Token(TokenType.STAR, "*", null, 1), new Expr.Grouping(new
// Expr.Literal(45.67)));
// System.out.println(new AstPrinter().print(expression));

// }
// }

// class RpnPrinter implements Visitor<String> {
// String print(Expr expr) {
// return expr.accept(this);
// }

// @Override
// public String visitBinaryExpr(Binary expr) {
// return rpn(expr.operator.lexeme, expr.left, expr.right);
// }

// @Override
// public String visitGroupingExpr(Grouping expr) {
// return rpn("group", expr.expression);
// }

// @Override
// public String visitLiteralExpr(Expr.Literal expr) {
// if (expr.value == null)
// return "nil";
// return expr.value.toString();
// }

// @Override
// public String visitUnaryExpr(Expr.Unary expr) {
// return rpn(expr.operator.lexeme, expr.right);
// }

// private String rpn(String name, Expr... exprs) {
// StringBuilder builder = new StringBuilder();

// for (Expr expr : exprs) {
// builder.append(expr.accept(this));
// builder.append(" ");
// }

// builder.append(name);

// return builder.toString();
// }

// @Override
// public String visitTernaryExpr(Ternary expr) {
// return rpn(expr.operator.lexeme, expr.first, expr.second, expr.third);
// }

// public static void main(String[] args) {
// Expr expression = new Binary(
// new Binary(new Expr.Literal(1), new Token(TokenType.PLUS, "+", null, 1), new
// Expr.Literal(2)),
// new Token(TokenType.STAR, "*", null, 1),
// new Binary(new Expr.Literal(4), new Token(TokenType.MINUS, "-", null, 1), new
// Expr.Literal(3)));
// System.out.println(new RpnPrinter().print(expression));

// }
// }