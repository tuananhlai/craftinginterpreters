package com.craftint.lox;

class RuntimeError extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -6644539315743479973L;
    final Token token;

    RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }
}
