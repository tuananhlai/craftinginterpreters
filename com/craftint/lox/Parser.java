package com.craftint.lox;

import java.util.List;

import static com.craftint.lox.TokenType.*;

class Parser {
    private static class ParseError extends RuntimeException {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
    }

    private final List<Token> tokens;
    private int current = 0; // pointer to next token

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    Expr parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null;
        }
    }

    private Expr expression() {
        return comma();
    }

    /**
     * Support comma operator (ex. a = 1, b += a )
     * 
     * @return
     */
    private Expr comma() {
        Expr expr = ternary();

        while (match(COMMA)) {
            Token operator = previous();
            Expr right = ternary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    /**
     * Support ternary operator (ex. 1 == 1 ? 1 : 2)
     * 
     * First, parse the firse expr with equality() to ensure right-associativity.
     * Then, if the next token is a QuestionToken, parse the second expr
     * recursively. Then, scan for a colon, if no colon found or the parser has
     * reached EOF, throw error. Finally, return new ternary expr with TERNARY
     * operator.
     * 
     * The precedence of ternary operator is based on
     * https://en.cppreference.com/w/c/language/operator_precedence
     * 
     * @return
     */
    private Expr ternary() {
        Expr condition = equality();

        if (match(QUESTION)) {
            Expr second = ternary();
            consume(COLON, "Expect ':'");
            if (isAtEnd()) {
                throw error(peek(), "Expect expression for ternary operator");
            }
            return new Expr.Ternary(new Token(TERNARY, "?:", null, peek().line), condition, second, ternary());
        }

        return condition;
    }

    private Expr equality() {
        Expr expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr comparison() {
        Expr expr = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() {
        Expr expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary() {
        Expr expr = primary();

        while (match(BANG, MINUS)) {
            Token operator = previous();
            expr = new Expr.Unary(operator, expr);
        }

        return expr;
    }

    private Expr primary() {
        if (match(FALSE))
            return new Expr.Literal(false);
        if (match(TRUE))
            return new Expr.Literal(true);
        if (match(NIL))
            return new Expr.Literal(null);

        if (match(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        throw error(peek(), "Expect expression.");
    }

    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();

        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    /**
     * Discard all the tokens up until the next statement
     */
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) {
                return;
            }

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
                default:
                    break;
            }

            advance();
        }
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }

        return peek().type == type;
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token advance() {
        if (!isAtEnd())
            current++;

        return previous();
    }
}
