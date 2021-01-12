package com.craftint.lox;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment
 */
class Environment {
    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    Environment() {
        enclosing = null;
    }

    Environment(Environment env) {
        enclosing = env;
    }

    /**
     * Call when user define new variable
     * 
     * NOTE: Redefining variable is legal with this function
     * 
     * var a = 1; var a = 2;
     * 
     * @param name
     * @param value
     */
    void define(String name, Object value) {
        values.put(name, value);
    }

    /**
     * Handle assignment expression. Only previously-defined var can be assigned.
     * 
     * @param name
     * @param value
     */
    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            define(name.lexeme, value);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable: '" + name.lexeme + "'.");
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (enclosing != null)
            return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }
}